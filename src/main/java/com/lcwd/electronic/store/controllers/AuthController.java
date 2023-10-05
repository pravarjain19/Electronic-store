package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.security.JwtHelper;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtHelper jwtHelper;

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrenUser(Principal principal){
        String name = principal.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);

        return  new ResponseEntity<>( modelMapper.map(userDetails , UserDto.class)  , HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@RequestBody JwtRequest request){
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userDto(userDto).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email , password);
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw  new ResourceNotFoundException("Invalid UserName and password");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private ModelMapper modelMapper = new ModelMapper();
}
