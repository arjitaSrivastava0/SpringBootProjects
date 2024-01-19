package com.student.management.service;

import com.student.management.Dto.UserRegistrationDto;
import com.student.management.entity.StudentEntity;
import com.student.management.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public UserEntity save(UserRegistrationDto registrationDto);

    public List<UserEntity> getAllUserDetails();

    void deleteUserByEmail(String email);

    UserEntity getUserByFirstname(String keyword);

    UserEntity getUserByEmail(String email);

    public void update(UserEntity userUpdateReqDto);

    UserEntity getByEmail(String email);

    Page<UserEntity> getAllStudents(Pageable pageable);


}
