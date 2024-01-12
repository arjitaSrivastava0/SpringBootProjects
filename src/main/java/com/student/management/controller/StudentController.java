package com.student.management.controller;

import com.student.management.entity.StudentEntity;
import com.student.management.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    // handler method to handle list students
    @GetMapping("/fetch/all")
    public List<StudentEntity> allStudents() {
        List<StudentEntity> student = studentService.getAllStudents();
        return student;
    }
//    @GetMapping("/list")
//    public String listStudents(Model model) {
//        log.info("studentService.getAllStudents(): "+studentService.getAllStudents());
//        model.addAttribute("studentlist", studentService.getAllStudents());
//        return "student";
//    }
    //by firstname
    @GetMapping("/fetch/firstname/{keyword}")
    public List<StudentEntity> getStudentByFirstName(@PathVariable String keyword) {
        List<StudentEntity> student = studentService.getStudentByFirstName(keyword);
        return student;
    }

    @GetMapping("/fetch/lastname/{keyword}")
    public List<StudentEntity> getStudentByLastName(@PathVariable String keyword) {
        List<StudentEntity> student = studentService.getStudentByLastName(keyword);
        return student;
    }
    //random word search
    @GetMapping("/fetch/contains/{letter}")
    public List<StudentEntity> getStudentByLetter(@PathVariable String letter) {
        List<StudentEntity> studentList = studentService.getStudentByFirstnameContains(letter);
        return studentList;
    }

    @GetMapping("/fetch/startswith/{keyword}")
    public List<StudentEntity> getStudentNameStartsWith(@PathVariable String keyword) {
        List<StudentEntity> studentList = studentService.getStudentNameStartsWith(keyword);
        return studentList;
    }
    @PostMapping("/register")
    public String saveStudent(@RequestBody StudentEntity student) {
        studentService.saveStudent(student);
        return "Record added.";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@RequestBody StudentEntity student, @PathVariable Long id) {

        // get student from database by id
        StudentEntity existingStudent = studentService.getStudentById(id);
        existingStudent.setId(id);
        existingStudent.setFirstname(student.getFirstname());
        existingStudent.setLastname(student.getLastname());
        existingStudent.setEmail(student.getEmail());

        // save updated student object
        studentService.updateStudent(existingStudent);
        return "updated successfully";
    }

    // handler method to handle delete student request

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@RequestBody Long id) {
        studentService.deleteStudentById(id);
        return "deleted successfully";
    }
}

