package com.hms.controller;

import com.hms.dto.AdminDto;
import com.hms.dto.LoginDto;
import com.hms.security.JwtAuthResponse;
import com.hms.security.JwtTokenHelper;
import com.hms.service.AdminService;
import com.hms.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/hms")
@Slf4j
public class LoginController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        log.info("Login API Triggered");
        Authentication authentication=null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (AuthenticationException e){
            log.info("Error in user authentication");
            return new ResponseEntity<>(new ApiResponse("User Not Authorized. Possibly Bad Credentials",false), HttpStatus.UNAUTHORIZED);
        }
        log.info("Authentication Object Created");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AdminDto user = adminService.findByEmail(userDetails.getUsername());
        String token = jwtTokenHelper.generateToken(userDetails.getUsername(), user.getId().toString());
        log.info("Token Generated");
        Date expiration = jwtTokenHelper.getExpirationDateFromToken(token);
        return new ResponseEntity<>(new JwtAuthResponse(user,token,new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(expiration)),HttpStatus.OK);
    }

    @PutMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestParam String email){
        AdminDto admin = this.adminService.findByEmail(email);
        if(admin==null){
            return new ResponseEntity<>(new HashMap<>().put("result",false),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new HashMap<>().put("result",true),HttpStatus.OK);
    }

}
