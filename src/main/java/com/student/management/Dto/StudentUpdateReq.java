package com.student.management.Dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class StudentUpdateReq {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
}
