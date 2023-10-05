package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entites.Category;
import com.lcwd.electronic.store.entites.Product;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.UUID;
@Service
public class ProductServiceImpl implements ProductService {

  private   ModelMapper modelMapper = new ModelMapper();

  @Autowired
    ProductRepository productRepository;

    @Override
    public ProductDto create(ProductDto productDto) {
        String id  = UUID.randomUUID().toString();
        productDto.setAddedDate(new Date());
        productDto.setProductId(id);
        Product save = productRepository.save( modelMapper.map(productDto, Product.class));

        return modelMapper.map(save, ProductDto.class);
    }

    @Override
    public ProductDto getSingleProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow
                (() -> new RuntimeException("Product not found by id " + id));
        return modelMapper.map(product , ProductDto.class);
    }

    @Override
    public PageResponse<ProductDto> getAllProduct(int pageSize , int pageNumber  , String sortBy , String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
         Pageable pageable = PageRequest.of(pageNumber , pageSize , sort);
        Page<Product> all = productRepository.findAll(pageable);

        PageResponse<ProductDto> pageableResponse = Helper.getPageableResponse(all, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto , String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException());
        product.setAddedDate(productDto.getAddedDate());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setProductImageName(productDto.getProductImageName());
        Product saved = productRepository.save(product);


        return  modelMapper.map(saved , ProductDto.class);
    }

    @Override
    public void deleteProduct(String id) throws FileNotFoundException {
        Product singleProduct = productRepository.findById(id).orElseThrow(()-> new RuntimeException());
        Boolean b = fileService.deleteFile(imagePath, singleProduct.getProductImageName());
        logger.info("Product Deleted {} "  , b);
        productRepository.deleteById(id);
    }

    @Override
    public PageResponse<ProductDto> getByProductByKeyword(String keyword  , int pageSize , int pageNumber  , String sortBy , String sortDir) {
       Sort sort = (sortDir.equalsIgnoreCase("desc"))
               ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber , pageSize  , sort);

        Page<Product> bytitleContaining = productRepository.findBytitleContaining(keyword , pageable);
        PageResponse<ProductDto> pageableResponse = Helper.getPageableResponse(bytitleContaining, ProductDto.class);
        return  pageableResponse;
    }

    @Override
    public PageResponse<ProductDto> getAllLiveProduct(int pageSize , int pageNumber  , String sortBy , String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber , pageSize  , sort);

        Page<Product> byLiveTrue = productRepository.findByLiveTrue(pageable);
        PageResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byLiveTrue, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(()
                -> new RuntimeException("Category NOt found" + categoryId));

        Product product = modelMapper.map(productDto , Product.class);

        String uuid = UUID.randomUUID().toString();
        product.setProductId(uuid);
        product.setAddedDate(new Date());
        product.setCategory(category);

        Product saveProduct = productRepository.save(product);

        return modelMapper.map(saveProduct , ProductDto.class);
    }
    //    update category  of product
    @Override
    public ProductDto updateCategory(String categoryId, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new RuntimeException("Product not found by id :" + productId));

        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new RuntimeException("Category not found by id" + categoryId));



        product.setCategory(category);

        Product saved = productRepository.save(product);

        return  modelMapper.map(saved , ProductDto.class);


    }

    @Override
    public PageResponse<ProductDto> getAllProductByCategory(
            String cid, int pageSize, int pageNumber, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(cid).orElseThrow(() -> new RuntimeException("Not category found" + cid));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
                : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber , pageSize , sort);

        Page<Product> byCategory = productRepository.findByCategory(category, pageable);

        PageResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byCategory, ProductDto.class);


        return pageableResponse;
    }

//    update category  of product




    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private  String imagePath;

    private Logger logger    = LoggerFactory.getLogger(ProductServiceImpl.class);

}
