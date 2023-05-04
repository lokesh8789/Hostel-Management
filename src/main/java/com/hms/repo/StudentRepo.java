package com.hms.repo;

import com.hms.dto.StudentDto;
import com.hms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student,Integer> {

    List<Student> findByFirstName(String firstName);

    Student findByEmail(String email);

}
