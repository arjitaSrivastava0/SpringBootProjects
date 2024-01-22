package com.student.management.controller;

import com.student.management.Dto.UserRegistrationDto;
import com.student.management.entity.StudentEntity;
import com.student.management.entity.UserEntity;
import com.student.management.service.StudentService;
import com.student.management.service.UserService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final StudentService studentService;

    @GetMapping("/addnew")
    public String addNewStudent( ModelMap model) {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        model.addAttribute("newuser", userRegistrationDto);
        return "addstudent";
    }

    @GetMapping("/newuser")
    public String newuser(@Valid @ModelAttribute("newuser") UserRegistrationDto userUpdateReqDto,
                          BindingResult result, ModelMap model) {
        if(result.hasErrors()){
            return "admin";
        }
        UserEntity userFromDb = userService.getUserByEmail(userUpdateReqDto.getEmail());
        log.info(userFromDb.toString()+" userfromdb ");
        if(userFromDb == null){
            log.info("called newuser");
            userService.save(userUpdateReqDto);
            return "redirect:/admin/list";
        }
       model.addAttribute("message","Email id already exists.");
        return "addstudent";

    }

    @GetMapping("/uploadpage")
    public String loadUploadPage() {
        return "upload";
    }

    @GetMapping("/list")
    public String listStudents( @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10",required = false) int size,
                                 @RequestParam(defaultValue = "firstname") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortOrder,
                                 Model model) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Page<UserEntity> studentPage = userService.getAllStudents(PageRequest.of(page, size, sort));

        model.addAttribute("adminlist", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "admin";
    }

    @GetMapping("/save")
    public String saveUser(@Valid @ModelAttribute("edituser") UserEntity userUpdateReqDto, BindingResult result, ModelMap model)
    {
        log.info("userUpdateReqDto: "+userUpdateReqDto.getFirstname()+" "+userUpdateReqDto.getEmail()
        +"   "+userUpdateReqDto.getIsadmin()+"   "+userUpdateReqDto.getPassword());
        if(result.hasErrors()){
            return "admin";
        }
        userService.update(userUpdateReqDto);
        //studentService.saveStudent(std);
        return "redirect:/admin/list";
    }

    @GetMapping("/fetchbyfirstname")
    public String getStudentByFirstName(String keyword, ModelMap model) {
        log.info("keyword: "+keyword);
        model.addAttribute("adminlist", userService.getUserByFirstname(keyword));
        return "admin";
    }

    @GetMapping("/edit/{email}")
    public String showEditStudentPage(@PathVariable(name = "email")String email, Model model) {
        log.info("email from path variable: "+email);
        UserEntity std = userService.getByEmail(email);
        log.info("first "+std.getFirstname()+" last "
                +std.getLastname()+" email "+std.getEmail());
        model.addAttribute("edituser", std);
        return "updateuser";
    }
    @GetMapping("/delete/{email}")
    public String deleteStudentPage(@PathVariable(name = "email") String email) {
        log.info("email delete: "+email);
        userService.deleteUserByEmail(email);
//        studentService.deleteStudentByEmail(email);
        return "redirect:/admin/list";
    }

    // Example using Apache POI for Excel processing
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,Model model) {
        try {
            //TODO: validation check for existing email.
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            int count = 0;
            int totalrecord = 0;
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {

                    String email = row.getCell(0).getStringCellValue();
                    String firstname = row.getCell(1).getStringCellValue();
                    String isadmin = row.getCell(2).getStringCellValue();
                    String lastname = row.getCell(3).getStringCellValue();
                    String password= row.getCell(4).getStringCellValue();

                    //Email existence check
                    UserEntity existingUser = userService.getUserByEmail(email);
                    log.info("email from file:    "+email);
//                    log.info("existingUser: "+existingUser.getEmail());
                    totalrecord++;
                    if(existingUser == null ){
                        log.info("no email present");
                        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
                        userRegistrationDto.setEmail(email);
                        userRegistrationDto.setFirstName(firstname);
                        userRegistrationDto.setLastName(lastname);
                        userRegistrationDto.setIsadmin(isadmin);
                        userRegistrationDto.setPassword(password);

                        userService.save(userRegistrationDto);
                        count++;
//                        if(isadmin.equals("no")) {
//                            StudentEntity student = new StudentEntity();
//                            student.setEmail(email);
//                            student.setFirstname(firstname);
//                            student.setLastname(lastname);
//                            studentService.saveStudent(student);
//                        }
                    }else {
                        log.info("No record uploaded because email id already exists.");
                    }

                }
            }
            log.info("count: "+count+" totalrows: "+totalrecord);
            model.addAttribute("message", count+" record uploaded successfully: " + (totalrecord-count)+" records failed email id already exists"+ file.getOriginalFilename());
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
            e.printStackTrace();
        }
        return "uploadresponse";
    }

    @GetMapping("/download")
    public void downloadStudents(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10",required = false) int size,
                                 @RequestParam(defaultValue = "firstname") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortOrder,
                                 HttpServletResponse response) throws IOException {
//        List<UserEntity> students = userService.getAllUserDetails();
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Page<UserEntity> studentPage = userService.getAllStudents(PageRequest.of(page, size, sort));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("userid");
        headerRow.createCell(1).setCellValue("email");
        headerRow.createCell(2).setCellValue("firstname");
        headerRow.createCell(3).setCellValue("isadmin");
        headerRow.createCell(4).setCellValue("lastname");
        headerRow.createCell(5).setCellValue("password");

        // Populate data rows
        int rowNum = 1;
        for (UserEntity student : studentPage.getContent()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getUserid());
            row.createCell(1).setCellValue(student.getEmail());
            row.createCell(2).setCellValue(student.getFirstname());
            row.createCell(3).setCellValue(student.getIsadmin());
            row.createCell(4).setCellValue(student.getLastname());
            row.createCell(5).setCellValue(student.getPassword());
        }

        // Set the response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        // Write the workbook to the response output stream
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }




}
