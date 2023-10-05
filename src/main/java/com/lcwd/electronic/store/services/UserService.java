package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;


public interface UserService {

//    create

    UserDto saveUser(UserDto userDto);
    
//    update
    UserDto updateUser(UserDto userDto , String userId);
//    get by single user by id
    UserDto getUserById(String id);

//    delete user by id
    void deleteUserById(String id) throws FileNotFoundException;
//    get all user
    PageResponse<UserDto> getAllUser(int pageSize , int pageNumber  , String sortBy , String sortDir);
//    get user by email
    UserDto getUserByEmail(String email);
//    search user
    public List<UserDto> searchUser(String keyword);

//    other user specfic feature


}
