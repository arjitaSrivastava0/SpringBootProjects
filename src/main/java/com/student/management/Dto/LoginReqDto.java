package com.student.management.Dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoginReqDto {
    private String email;
    private String password;
}
