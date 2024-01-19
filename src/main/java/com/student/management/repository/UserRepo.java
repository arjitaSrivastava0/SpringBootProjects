package com.student.management.repository;

import com.student.management.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);


    List<UserEntity> findAll();

    void deleteByEmail(String email);

    UserEntity findByFirstname(String keyword);

    UserEntity getUserByEmail(String email);


}
