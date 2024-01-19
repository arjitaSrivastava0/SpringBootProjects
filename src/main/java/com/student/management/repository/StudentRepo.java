package com.student.management.repository;

import com.student.management.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepo extends JpaRepository<StudentEntity,Long> {

    List<StudentEntity> findByFirstnameStartingWith(String keyword);
    List<StudentEntity> findByFirstnameLike(String keyword);
    List<StudentEntity> findByLastnameLike(String keyword);

    List<StudentEntity> findByFirstnameContaining(String letter);

    void deleteByEmail(String email);

    StudentEntity getStudentByEmail(String email);
}
