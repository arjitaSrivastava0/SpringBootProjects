package com.student.management.controller;

import com.student.management.Dto.StudentUpdateReq;
import com.student.management.entity.StudentEntity;
import com.student.management.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
@Slf4j
public class StudentMvcController {

    private final StudentService studentService;

    @GetMapping("/addnew")
    public String addNewStudent( ModelMap model) {
        StudentEntity student = new StudentEntity();
        model.addAttribute("newstudent", student);
        return "addstudent";
    }

    @GetMapping("/list/{email}")
    public String listStudents(@PathVariable(name = "email") String email, ModelMap model) {
        log.info("email mvccontroller: "+email);
        log.info("studentService.getAllStudents(): "+studentService.getStudentByEmail(email));
        model.addAttribute("studentlist", studentService.getStudentByEmail(email));
        return "student";
    }

    @GetMapping("/fetchbyfirstname")
    public String getStudentByFirstName(String keyword, ModelMap model) {
        log.info("keyword: "+keyword);
        model.addAttribute("studentlist", studentService.getStudentByFirstName(keyword));
        return "student";
    }

    @GetMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("newstudent") StudentEntity std, BindingResult result, ModelMap model)
    {
        if(result.hasErrors()){
            return "addstudent";
        }
       studentService.saveStudent(std);
        //studentService.saveStudent(std);
        return "redirect:/student/list";
    }

    @PostMapping("/updatestudent")
    public String updateStudent(@ModelAttribute StudentUpdateReq studentEntity,
                                BindingResult bindingResult,Model model) {
        log.info("student entity: "+studentEntity.getEmail());
        StudentEntity existingStudent = studentService.getStudentByEmail(studentEntity.getEmail());
        existingStudent.setFirstname(studentEntity.getFirstname());
        existingStudent.setLastname(studentEntity.getLastname());
        studentService.updateStudent(existingStudent);
        return "redirect:/student/list/"+existingStudent.getEmail();
    }

    @GetMapping("/edit/{email}")
    public String showEditStudentPage(@PathVariable(name = "email")String email, Model model) {
        StudentEntity std = studentService.getStudentByEmail(email);
        model.addAttribute("editstudent", std);
        return "update";
    }
    @GetMapping("/delete/{id}")
    public String deleteStudentPage(@PathVariable(name = "id") Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/student/list";
    }
}
