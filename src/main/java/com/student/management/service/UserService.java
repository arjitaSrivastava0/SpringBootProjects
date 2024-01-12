package com.student.management.service;

import com.student.management.Dto.UserRegistrationDto;
import com.student.management.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserEntity save(UserRegistrationDto registrationDto);

    UserEntity getByEmailId(String email);

}
