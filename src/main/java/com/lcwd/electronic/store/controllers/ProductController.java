package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageApiResponseMessage;
import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    ProductService productService;
//    create product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct( @Valid  @RequestBody ProductDto productDto){
        ProductDto savedPrdocut = productService.create(productDto);
        
        return  new ResponseEntity<>(savedPrdocut , HttpStatus.CREATED);

    }
    
    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> getALlProducts(
            @RequestParam(required = false , value = "pageSize", defaultValue = "10") int pageSize ,
            @RequestParam(required = false , value = "pageNumber" , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , value = "sortBy" , defaultValue = "title") String sortBy ,
            @RequestParam(required = false , value = "sortDir" , defaultValue = "asc") String sortDir
    ){
        PageResponse<ProductDto> allProduct = productService.getAllProduct(pageSize, pageNumber, sortBy, sortDir);
        
        return new ResponseEntity<>(allProduct , HttpStatus.OK);
    }
    
    @GetMapping("/{pid}")
    public  ResponseEntity<ProductDto> getProduct(@PathVariable String pid){

        ProductDto singleProduct = productService.getSingleProduct(pid);
        return  new ResponseEntity<>(singleProduct , HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{pid}")
    public  ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String  pid) throws FileNotFoundException {
        productService.deleteProduct(pid);
        ApiResponseMessage productDeleteSuccessfully = ApiResponseMessage.builder().response(HttpStatus.OK)
                .status(true)
                .message("Product Delete successfully").build();

        return  new ResponseEntity<>(productDeleteSuccessfully , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{pid}")
    public  ResponseEntity<ProductDto> updateProduct(@PathVariable String pid , @RequestBody ProductDto productDto){

        ProductDto updatedProduct = productService.updateProduct(productDto, pid);

        return  new ResponseEntity<>(updatedProduct  , HttpStatus.CREATED);
    }

    @GetMapping("/search/{keyword}")
    public  ResponseEntity<PageResponse<ProductDto>> getAllProductByTitleSearch(
            @RequestParam(required = false , value = "pageSize", defaultValue = "10") int pageSize ,
            @RequestParam(required = false , value = "pageNumber" , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , value = "sortBy" , defaultValue = "title") String sortBy ,
            @RequestParam(required = false , value = "sortDir" , defaultValue = "asc") String sortDir,
            @PathVariable String keyword
    ){
        PageResponse<ProductDto> byProductByKeyword = 
                productService.getByProductByKeyword(keyword, pageSize, pageNumber, sortBy, sortDir);
        
        return  new ResponseEntity<>(byProductByKeyword , HttpStatus.OK);
    }
    
    @GetMapping("/live")
    public  ResponseEntity<PageResponse<ProductDto>> getAllLiveProduct(
            @RequestParam(required = false , value = "pageSize", defaultValue = "10") int pageSize ,
            @RequestParam(required = false , value = "pageNumber" , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , value = "sortBy" , defaultValue = "title") String sortBy ,
            @RequestParam(required = false , value = "sortDir" , defaultValue = "asc") String sortDir
    ){
        PageResponse<ProductDto> allLiveProduct =
                productService.getAllLiveProduct(pageSize, pageNumber, sortBy, sortDir);

        return new ResponseEntity<>(allLiveProduct , HttpStatus.OK);

    }
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/image/{id}")
        public ResponseEntity<ImageApiResponseMessage>  uploadProductImage(
                @PathVariable String id , @RequestParam("productImage") MultipartFile multipartFile) throws IOException {

            ProductDto singleProduct = productService.getSingleProduct(id);

            String imageName = fileService.uploadFile(multipartFile, imagePath);
            singleProduct.setProductImageName(imageName);

            productService.updateProduct(singleProduct , id);

            ImageApiResponseMessage imageUploaded = ImageApiResponseMessage.builder().imageName(imageName)
                    .message("Image uploaded")
                    .status(true)
                    .response(HttpStatus.CREATED).build();

            return  new ResponseEntity<>(imageUploaded , HttpStatus.CREATED);

        }

        @GetMapping("/image/{id}")
        public  void serveProductImage(@PathVariable String id , HttpServletResponse response) throws IOException {

            ProductDto singleProduct = productService.getSingleProduct(id);
            InputStream resource = fileService.getResource(imagePath, singleProduct.getProductImageName());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource , response.getOutputStream());

        }

        @Autowired
        private FileService fileService;

        @Value("${product.image.path}")
        private  String imagePath;

}
