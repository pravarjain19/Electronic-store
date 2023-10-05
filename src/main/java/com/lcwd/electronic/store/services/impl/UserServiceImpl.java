package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entites.Role;
import com.lcwd.electronic.store.entites.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements  UserService  {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


    private  String  roleId ="normal_tiuwbnxcmntqtbnjaqe";
    @Override
    public UserDto saveUser(UserDto userDto) {

//        generate unigque id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        userDto to entity
        User user = dtoToEntity(userDto);

//        role set
        Role role = roleRepository.findById(roleId).get();
        user.getRoles().add(role);


        User saveUser = userRepository.save(user);


//        entity to dto
        UserDto saveUserDto = entityTODto(saveUser);
        return saveUserDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto , String userId) {

//         get user by id
       User user = userRepository.findById(userId).orElseThrow(()->
               new ResourceNotFoundException("User not found with id:"+userId));

//       update the information
       user.setAbout(userDto.getAbout());
       user.setGender(userDto.getGender());
//       user.setPassword(userDto.getPassword());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
       user.setImageName(userDto.getImageName());
       user.setName(userDto.getName());

//       save the data
       User updatedUser = userRepository.save(user);


        return entityTODto(updatedUser);
    }

    @Override
    public UserDto getUserById(String id) {
       User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found by id :" + id));
       return  entityTODto(user);
    }

    @Override
    public void deleteUserById(String userid) throws FileNotFoundException {

        User user = userRepository.findById(userid)
                .orElseThrow(()->new ResourceNotFoundException("User not found : " + userid));
       Boolean ss =  fileService.deleteFile(imageUploadPath , user.getImageName());
       logger.info("file delted : {}" ,ss);
        userRepository.delete(user);

    }

    @Override
    public PageResponse<UserDto> getAllUser(int pageSize , int pageNumber  , String sortBy , String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
                : (Sort.by(sortBy).ascending());


        Pageable pageable = PageRequest.of(pageNumber , pageSize ,sort);

       Page<User> page =  userRepository.findAll(pageable);

       List<User> allUser=  userRepository.findAll(pageable).getContent();

       PageResponse<UserDto> response = Helper.getPageableResponse(page,UserDto.class);


        return response;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).
                orElseThrow(()->new ResourceNotFoundException("User not by the email " + email));
        return entityTODto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> userList = userRepository.findByNameContaining(keyword);
       List<UserDto> userDtoList = userList.stream().map(this::entityTODto).collect(Collectors.toList());
        return userDtoList;
    }

    private UserDto entityTODto(User saveUser) {

//        covert entity to Dto
//        return UserDto.builder()
//                .userId(saveUser.getUserId())
//                .name(saveUser.getName())
//                .email(saveUser.getEmail())
//                .password(saveUser.getPassword())
//                .about(saveUser.getAbout())
//                .gender(saveUser.getGender())
//                .imageName(saveUser.getImageName()).build();
        return modelMapper.map(saveUser , UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {

//        used to covernt dto to entity
//     return User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .about(userDto.getAbout())
//                .email(userDto.getEmail())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .password(userDto.getPassword()).build();

        return modelMapper.map(userDto , User.class);



    }

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    FileService fileService;

    @Value("${user.profile.image.path}")
    private  String imageUploadPath;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

}
