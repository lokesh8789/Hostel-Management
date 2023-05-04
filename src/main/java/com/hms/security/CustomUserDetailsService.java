package com.hms.security;

import com.hms.entity.Student;
import com.hms.repo.StudentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("inside load user by user name");
        Student student = this.studentRepo.findByEmail(username);
        if(student == null){
            throw new UsernameNotFoundException("Student does not exist");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        log.info("returning userDetails");
        return new User(student.getEmail(),student.getPassword(),authorities);
    }
}
