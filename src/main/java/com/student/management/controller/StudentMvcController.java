package com.student.management.controller;

import com.student.management.Dto.StudentUpdateReq;
import com.student.management.entity.StudentEntity;
import com.student.management.entity.UserEntity;
import com.student.management.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.OutputStream;
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

    @PostMapping("/updatestudent/{email}")
    public String updateStudent(@ModelAttribute StudentUpdateReq studentEntity,
                                @PathVariable(name = "email") String email,
                                BindingResult bindingResult,Model model) {
        log.info("student entity: "+email);
        StudentEntity existingStudent = studentService.getStudentByEmail(email);

        if(existingStudent != null) {
            existingStudent.setEmail(existingStudent.getEmail());
            existingStudent.setFirstname(studentEntity.getFirstname());
            existingStudent.setLastname(studentEntity.getLastname());
            studentService.updateStudent(existingStudent);
        }else{
            log.info("email id cannot be updated.");
        }

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


    @GetMapping("/download/{email}")
    public void downloadStudents(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10",required = false) int size,
                                 @RequestParam(defaultValue = "firstname") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortOrder,
                                 @PathVariable(name = "email") String email,
                                 HttpServletResponse response) throws IOException {
//        List<UserEntity> students = userService.getAllUserDetails();
        log.info("email for download: "+email);
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Page<StudentEntity> studentPage = studentService.getStudentByEmailPaginated(email,PageRequest.of(page, size, sort));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("id");
        headerRow.createCell(1).setCellValue("email");
        headerRow.createCell(2).setCellValue("firstname");
        headerRow.createCell(4).setCellValue("lastname");

        // Populate data rows
        int rowNum = 1;
        for (StudentEntity student : studentPage.getContent()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getEmail());
            row.createCell(2).setCellValue(student.getFirstname());
            row.createCell(4).setCellValue(student.getLastname());
        }

        // Set the response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");

        // Write the workbook to the re/sponse output stream
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
