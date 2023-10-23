package com.lcwd.electronic.store.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.security.JwtHelper;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")


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


//     login with google api

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {
        ResponseEntity<JwtResponse> login = null;
//        id token from request
        String idToken = data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory defaultInstance = JacksonFactory.getDefaultInstance();

         GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport , defaultInstance).setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Paylod {}"  , payload);

        String email = payload.getEmail();

        UserDto userDto = null;

        try{
         userDto = userService.getUserByEmail(email);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(userDto == null){


//            create a new user
             userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setName(data.get("name").toString());
            userDto.setImageName(data.get("photoUrl").toString());
            userDto.setPassword(newPassword);
            userDto.setRoles(new HashSet<>());
            userService.saveUser(userDto);
             login = this.login(JwtRequest.builder().email(userDto.getEmail()).password(newPassword).build());

        }
        else {
            login = this.login(JwtRequest.builder().email(userDto.getEmail()).password(newPassword).build());

        }
        return  login;
    }

    private Logger logger = LoggerFactory.getLogger(AuthController.class.getName());
    private ModelMapper modelMapper = new ModelMapper();


    @Value("${googleClientId}")
    private  String googleClientId;
    @Value("${newPassword}")
    private String newPassword;
}
