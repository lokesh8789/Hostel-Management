package com.hms.controller;

import com.hms.dto.LoginDto;
import com.hms.security.CustomUserDetailsService;
import com.hms.security.JwtAuthenticationFilter;
import com.hms.security.JwtResponse;
import com.hms.security.JwtTokenHelper;
import com.hms.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hms/login")
@Slf4j
public class LoginController {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/")
    public ResponseEntity<?> loginApp(@RequestBody LoginDto loginDto){
        log.info("Login api triggered");
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
        }catch (AuthenticationException e){
            log.info("Error in authentication");
            return new ResponseEntity<>(new ApiResponse("User not Authorised",false), HttpStatus.UNAUTHORIZED);
        }
        log.info("Authentication obj created");
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(loginDto.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        log.info("token generated");
        JwtResponse response = new JwtResponse();
        response.setToken(token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
