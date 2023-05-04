package com.hms.controller;

import com.hms.dto.AdminDto;
import com.hms.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("hms/admin")
@Slf4j
public class AdminController {
    @Autowired
    private AdminService adminService;
    @PostMapping("/add")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody AdminDto adminDto){
        return ResponseEntity.ok().build();
    }
}
