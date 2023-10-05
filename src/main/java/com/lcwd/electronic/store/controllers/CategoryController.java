package com.lcwd.electronic.store.controllers;


import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.entites.Product;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {


    @Autowired
    CategoryService categoryService;
//        create

        @PostMapping
        public ResponseEntity<CategoryDto> createCategory(@Valid   @RequestBody CategoryDto categoryDto){



            CategoryDto categoryDto1 = categoryService.create(categoryDto);
            return  new ResponseEntity<>(categoryDto1 , HttpStatus.CREATED);
        }

//    get single
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String id){

        CategoryDto categoryDto = categoryService.get(id);
        return  new ResponseEntity<>(categoryDto , HttpStatus.OK);
    }
//    get all
    @GetMapping
    public ResponseEntity<PageResponse<CategoryDto>>  getAll(
            @RequestParam(required = false , value = "pageSize", defaultValue = "10") int pageSize ,
            @RequestParam(required = false , value = "pageNumber" , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , value = "sortBy" , defaultValue = "title") String sortBy ,
            @RequestParam(required = false , value = "sortDir" , defaultValue = "asc") String sortDir
    ){
        PageResponse<CategoryDto> all = categoryService.getAll(pageSize, pageNumber, sortBy, sortDir);

        return  new ResponseEntity<>(all , HttpStatus.OK);
    }

//    update
        @PutMapping("/{id}")
        public ResponseEntity<CategoryDto> updateCategory(
                @PathVariable String id , @RequestBody CategoryDto categoryDto){
            CategoryDto update = categoryService.update(categoryDto, id);
            return  new ResponseEntity<>(update  , HttpStatus.OK);
        }
//    delete
        @DeleteMapping("/{id}")
        public  ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String id ) throws FileNotFoundException {
            categoryService.delete(id);
            ApiResponseMessage categoryDeltedSucccessful = ApiResponseMessage.builder()
                    .message("Category Delted Succcessful ")
                    .status(true)
                    .response(HttpStatus.OK).build();

            return  new ResponseEntity<>(categoryDeltedSucccessful  , HttpStatus.OK);

        }

        @GetMapping("/search/{keyword}")
        public  ResponseEntity<List<CategoryDto>> getCategoryByKeyWord(@PathVariable String keyword){
            List<CategoryDto> categoryDtos = categoryService.searchCategory(keyword);
            return new ResponseEntity<>(categoryDtos , HttpStatus.OK);

        }

        @PostMapping("/image/{cid}")
        public ResponseEntity<ImageApiResponseMessage> uploadCategoryImage(
                @PathVariable String cid,@RequestParam("categoryImage")MultipartFile multipartFile) throws IOException{
            CategoryDto categoryDto = categoryService.get(cid);
            String imageName = fileService.uploadFile(multipartFile, imageUploadPath);

            ImageApiResponseMessage responseMessage=  ImageApiResponseMessage.builder().imageName( imageName)
                    .message("image saved")
                    .status(true)
                    .response(HttpStatus.OK).build();

            if(!categoryDto.getCoverImage().isEmpty()){
                fileService.deleteFile(imageUploadPath , categoryDto.getCoverImage());
            }
            categoryDto.setCoverImage(imageName);

            categoryService.update(categoryDto  , cid);

            return new ResponseEntity<>(responseMessage , HttpStatus.CREATED);
        }
        @GetMapping("/image/{cid}")
        public  void getCategoryImage(@PathVariable String cid  , HttpServletResponse response) throws IOException {
            CategoryDto categoryDto = categoryService.get(cid);

            InputStream resource = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());

            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource , response.getOutputStream());
        }

//        create product with category
        @PostMapping("/{categoryId}/products")
        public  ResponseEntity<ProductDto> createProductWithCategory(
                @PathVariable String categoryId  , @RequestBody ProductDto productDto){

            ProductDto withCategory = productService.createWithCategory(productDto, categoryId);
            return  new ResponseEntity<>(withCategory , HttpStatus.CREATED);
        }

        @PutMapping("/{cid}/products/{pid}")
        public ResponseEntity<ProductDto> updateCategoryOfProduct(
                @PathVariable String cid , @PathVariable String pid
        ){
            ProductDto productDto = productService.updateCategory(cid, pid);

            return  new ResponseEntity<>(productDto , HttpStatus.OK);
        }

        @GetMapping("/{cid}/products")
        public  ResponseEntity<PageResponse<ProductDto>> getAllProductsByCategory(
                @PathVariable String cid,
                @RequestParam(required = false , value = "pageSize", defaultValue = "10") int pageSize ,
                @RequestParam(required = false , value = "pageNumber" , defaultValue = "0") int pageNumber,
                @RequestParam(required = false , value = "sortBy" , defaultValue = "title") String sortBy ,
                @RequestParam(required = false , value = "sortDir" , defaultValue = "asc") String sortDir

        ){
            PageResponse<ProductDto> allProductByCategory =
                    productService.getAllProductByCategory(cid, pageSize, pageNumber, sortBy, sortDir);

            return  new ResponseEntity<>(allProductByCategory  , HttpStatus.OK);
        }
    @Autowired
    private FileService fileService;
        @Autowired
        private ProductService productService;

    @Value("${category.image.path}")
    private  String imageUploadPath;
}
