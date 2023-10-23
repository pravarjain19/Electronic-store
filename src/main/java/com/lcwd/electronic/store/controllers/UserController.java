package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageApiResponseMessage;
import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.validation.Validator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api(description = "This is used to perform user related operations")
public class UserController {

    @Value("${user.profile.image.path}")
    private  String imageUploadPath;

    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable String id){

        return new ResponseEntity<>(userService.getUserById(id) , HttpStatus.OK);
    }

//    create user
    @PostMapping
    public ResponseEntity<UserDto> createUser( @Valid  @RequestBody UserDto userDto){
        UserDto savedUser  = userService.saveUser(userDto);
        return  new ResponseEntity<>(savedUser , HttpStatus.CREATED);
    }

//    update User
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser( @Valid
            @RequestBody UserDto userDto , @PathVariable String userId){

        UserDto savedUser  = userService.updateUser(userDto , userId);
        return  new ResponseEntity<>(savedUser , HttpStatus.OK);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws FileNotFoundException {

        userService.deleteUserById(userId);

       ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
               .message("Deleted successfully")
               .status(true)
               .response(HttpStatus.OK)
               .build();
        return new ResponseEntity<>(apiResponseMessage , HttpStatus.OK);

    }

    @ApiOperation(value = "Get all user " , tags = "User api")
    @ApiResponses(value = {
            @ApiResponse(code = 200 , message = "Success"),
            @ApiResponse(code = 400 , message = "Not authorized")
    })
    @GetMapping
    public  ResponseEntity<PageResponse<UserDto>> getAllUser(
            @RequestParam(required = false , value = "pageSize", defaultValue = "10") int pageSize ,
            @RequestParam(required = false , value = "pageNumber" , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , value = "sortBy" , defaultValue = "name") String sortBy ,
            @RequestParam(required = false , value = "sortDir" , defaultValue = "asc") String sortDir
    ){
     return  new ResponseEntity<>(userService.getAllUser(pageSize , pageNumber , sortBy , sortDir) , HttpStatus.OK);
    }

    @GetMapping("/email/{emailId}")
    public  ResponseEntity<UserDto> getUserByEmailId(@PathVariable String emailId){
        return  new ResponseEntity<>(userService.getUserByEmail(emailId) , HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public  ResponseEntity<List<UserDto>> searchUserByKeyword(@PathVariable String keyword){
        return  new ResponseEntity<>(userService.searchUser(keyword) , HttpStatus.OK);
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageApiResponseMessage> uploadUserImage(@PathVariable  String userId , @RequestParam("userImage")MultipartFile multipartFile) throws IOException {


       String imageName =  fileService.uploadFile(multipartFile , imageUploadPath);

   ImageApiResponseMessage responseMessage=  ImageApiResponseMessage.builder().imageName( imageName)
                .message("image saved")
               .status(true)
               .response(HttpStatus.OK).build();

        UserDto user =  userService.getUserById(userId);
        if(!user.getImageName().isEmpty()){
            fileService.deleteFile(imageUploadPath , user.getImageName());
        }
        user.setImageName(imageName);
       UserDto userUpdate =  userService.updateUser(user,userId);




       return new ResponseEntity<>(responseMessage , HttpStatus.CREATED);
    }

//    server user image

        @GetMapping("/image/{userId}")
        public void serverUserImage(@PathVariable String userId , HttpServletResponse response) throws IOException {

            UserDto userDto = userService.getUserById(userId);
            logger.info("User image name : {}" , userDto.getImageName());

           InputStream userImage =  fileService.getResource(imageUploadPath , userDto.getImageName());

           response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(userImage  , response.getOutputStream());
        }

      private final  Logger logger = LoggerFactory.getLogger(UserController.class);
}
