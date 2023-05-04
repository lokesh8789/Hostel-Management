package com.hms.service;

import com.hms.dto.AdminDto;

public interface AdminService {
    AdminDto findByEmail(String email);
}
