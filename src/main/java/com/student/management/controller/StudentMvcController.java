package com.student.management.controller;

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

    @GetMapping("/list")
    public String listStudents(ModelMap model) {
        log.info("studentService.getAllStudents(): "+studentService.getAllStudents());
        model.addAttribute("studentlist", studentService.getAllStudents());
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

    @GetMapping("/edit/{id}")
    public String showEditStudentPage(@PathVariable(name = "id")Long id, Model model) {
        StudentEntity std = studentService.getStudentById(id);
        model.addAttribute("editstudent", std);
        return "update";
    }
    @GetMapping("/delete/{id}")
    public String deleteStudentPage(@PathVariable(name = "id") Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/student/list";
    }
}
