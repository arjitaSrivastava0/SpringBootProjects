package com.student.management.service;

import com.student.management.entity.StudentEntity;
import com.student.management.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepo studentRepo;

    public StudentEntity saveStudent(StudentEntity student) {
        return studentRepo.save(student);
    }


    //fetch students methods
    public Page<StudentEntity> getAllStudents(Pageable pageable) {
        return studentRepo.findAll(pageable);
    }

    public StudentEntity getStudentById(Long id) {
        return studentRepo.findById(id).get();
    }

    public List<StudentEntity> getStudentByFirstName(String keyWord) {
        List<StudentEntity> listOfStudent = studentRepo.findByFirstnameLike(keyWord);
        log.info("findByFirstnameLike: "+listOfStudent.toString());
        return listOfStudent;
    }

    public List<StudentEntity> getStudentByLastName(String keyWord) {
        List<StudentEntity> listOfStudent = studentRepo.findByLastnameLike(keyWord);
        log.info("findByFirstnameLike: "+listOfStudent.toString());
        return listOfStudent;
    }

    public List<StudentEntity> getStudentNameStartsWith(String keyWord) {
        List<StudentEntity> studentRes = studentRepo.findByFirstnameStartingWith(keyWord);
        log.info("findByFirstnameContaining: "+studentRes.toString());
        return studentRes;
    }

    public List<StudentEntity> getStudentByFirstnameContains(String letter) {
        List<StudentEntity> studentRes = studentRepo.findByFirstnameContaining(letter);
        log.info("findByFirstnameContaining: "+studentRes.toString());
        return studentRes;
    }

    public StudentEntity updateStudent(StudentEntity student) {
        return studentRepo.save(student);
    }

    public void deleteStudentById(Long id) {
        studentRepo.deleteById(id);
    }

    public void deleteStudentByEmail(String email) {
        studentRepo.deleteByEmail(email);
    }

    public StudentEntity getStudentByEmail(String email){
        return studentRepo.getStudentByEmail(email);
    }

    public Page<StudentEntity> getStudentByEmailPaginated(String email, Pageable pageable){
        return studentRepo.getStudentByEmail(email,pageable);
    }
}

