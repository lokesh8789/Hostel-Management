package com.hms.service;

import com.hms.dto.StudentDto;
import com.hms.entity.Student;
import com.hms.exceptions.ResourceNotFoundException;
import com.hms.exceptions.UserDoesNotExistException;
import com.hms.exceptions.UserExistException;
import com.hms.repo.StudentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StudentRepo studentRepo;

    //to create student in DB
    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student user = studentRepo.findByRoll(studentDto.getRoll());
        if(user != null){
            throw new UserExistException("User Already Exist");
        }
        Student student = this.modelMapper.map(studentDto, Student.class);
        Student save = this.studentRepo.save(student);
        return this.modelMapper.map(save,StudentDto.class);
    }

    //to update student
    @Override
    public StudentDto updateStudent(StudentDto studentDto, int studentId) {
        Student student = this.studentRepo.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setMiddleName(studentDto.getMiddleName());
        student.setEmail(studentDto.getEmail());
        student.setRoll(studentDto.getRoll());
        Student save = this.studentRepo.save(student);
        return this.modelMapper.map(save,StudentDto.class);
    }

    //to get all student form db
    @Override
    public List<StudentDto> getAllStudent() {
        List<Student> students = this.studentRepo.findAll();
        List<StudentDto> studentDtos = students.stream().map(student -> this.modelMapper.map(student, StudentDto.class)).collect(Collectors.toList());
        return studentDtos;
    }

    //to get a student with id
    @Override
    public StudentDto getStudent(int studentId) {
        Student student = this.studentRepo.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        return this.modelMapper.map(student,StudentDto.class);
    }

    //to delete a student
    @Override
    public void deleteStudent(int studentId) {
        Student student = this.studentRepo.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        this.studentRepo.delete(student);
    }

    //find student by first name
    @Override
    public List<StudentDto> getByFirstName(String firstName) {
        List<Student> students = this.studentRepo.findByFirstName(firstName);
        return students.stream().map(student -> this.modelMapper.map(student, StudentDto.class)).collect(Collectors.toList());
    }

    //find student by state name
    @Override
    public List<StudentDto> getByStateName(String state) {
        List<Student> students = this.studentRepo.findByState(state);
        return students.stream().map(student -> this.modelMapper.map(student, StudentDto.class)).collect(Collectors.toList());
    }

    @Override
    public StudentDto getByRoll(String roll) {
        Student student = studentRepo.findByRoll(roll);
        if(student==null){
            throw new UserDoesNotExistException("User Not Found");
        }
        return modelMapper.map(student,StudentDto.class);
    }
}
