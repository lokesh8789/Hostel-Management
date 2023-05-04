package com.hms.service;

import com.hms.dto.AdminDto;
import com.hms.entity.Admin;
import com.hms.repo.AdminRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public AdminDto findByEmail(String email) {
        Admin user = adminRepo.findByEmail(email);
        return modelMapper.map(user,AdminDto.class);
    }
}
