package com.student.management.controller;

import com.student.management.Dto.LoginReqDto;
import com.student.management.entity.UserEntity;
import com.student.management.repository.UserRepo;
import com.student.management.service.UserService;
import com.student.management.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
    private final UserServiceImpl userImpl;
    @GetMapping("/login")

    public String login() {
        log.info("CALLED LOGIN METHOD /");
        return "login";
    }

//    @PostMapping("/login")
//    public String verifyLoggedUser(@RequestBody UserEntity user) {
//        UserEntity existingUserDetails = userService.getByEmailId(user.getEmail());
////        if(existingUserDetails.getRoles() == )
//
//        return "";
//    }

    @PostMapping("login/verify")

    public String login( @ModelAttribute("loginuser")@RequestBody LoginReqDto loginReqDto, BindingResult bindingResult, Model model) {
        log.info("CALLED METhod......");
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginuser", "Invalid credentials");
            return "login";
        }
        UserDetails loggedInUser = userImpl.loadUserByUsername(loginReqDto);
        log.info("loggedInUser:   "+loggedInUser);
        if (loggedInUser.getPassword().equals(loginReqDto.getPassword())) {
            log.info("loginReqDto.getRoles():   "+loggedInUser.getAuthorities().toString());
            if(!loggedInUser.getAuthorities().equals("ADMIN")){
                log.info("not admin");
//                model.addAttribute("studentpage","student");
                return "redirect:student/list";
            }else {
                log.info("this is admin");
                return "redirect:student/list";
            }
        }

        model.addAttribute("loginuser", "Invalid credentials");
        log.info("called invalid");
        return "redirect:/login";
    }
}
