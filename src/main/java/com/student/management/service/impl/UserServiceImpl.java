package com.student.management.service.impl;

import com.student.management.Dto.LoginReqDto;
import com.student.management.Dto.UserRegistrationDto;
import com.student.management.entity.StudentEntity;
import com.student.management.entity.UserEntity;
import com.student.management.entity.UserRole;
import com.student.management.repository.StudentRepo;
import com.student.management.repository.UserRepo;
import com.student.management.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepo userRepository;

    private final StudentRepo studentRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserRegistrationDto registrationDto) {
        UserEntity user;
        String isadmin = registrationDto.getIsadmin();
        log.info("registrationDto   "+registrationDto);
        if(isadmin.toUpperCase().equals("YES")) {
            user = new UserEntity(registrationDto.getFirstName(),
                    registrationDto.getLastName(), registrationDto.getEmail(),
                    registrationDto.getPassword(),registrationDto.getIsadmin(),
                    Arrays.asList(new UserRole("ADMIN")));
        } else {
            user = new UserEntity(registrationDto.getFirstName(),
                    registrationDto.getLastName(), registrationDto.getEmail(),
                    registrationDto.getPassword(),registrationDto.getIsadmin(),
                    Arrays.asList(new UserRole("USER")));
        }

        log.info("USER AFTER SETTING DATA:  "+user);

        if(!isadmin.toUpperCase().equals("YES")){
            StudentEntity student = new StudentEntity();
            student.setFirstname(user.getFirstname());
            student.setLastname(user.getLastname());
            student.setEmail(user.getEmail());
            studentRepo.save(student);
            log.info("Data saved in students table....");
        }
        log.info("data saved in user...");
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUserDetails() {
        return userRepository.findAll();
    }

    @Override
    public Page<UserEntity> getAllStudents(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void deleteUserByEmail(String email) {
        studentRepo.deleteByEmail(email);
        userRepository.deleteByEmail(email);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        log.info("email from fileupload: "+email);
        return userRepository.getUserByEmail(email);
    }

    @Override
    public void update(UserEntity userUpdateReqDto) {
        UserEntity existingUser = userRepository.getUserByEmail(userUpdateReqDto.getEmail());
        log.info("updatereq: "+userUpdateReqDto.getEmail());
        if(existingUser != null){
//            UserEntity updatedUser = new UserEntity();
            existingUser.setFirstname(userUpdateReqDto.getFirstname());
            existingUser.setLastname(userUpdateReqDto.getLastname());
            existingUser.setEmail(existingUser.getEmail());
            existingUser.setIsadmin(userUpdateReqDto.getIsadmin());
            existingUser.setPassword(existingUser.getPassword());
            userRepository.save(existingUser);
        }
    }


    @Override
    public UserEntity getByEmail(String email) {
        log.info("email service level: "+email);
        UserEntity userfromdb = userRepository.findByEmail(email);
        log.info("userfromdb:  "+userfromdb.getEmail());
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity getUserByFirstname(String keyword) {
        return userRepository.findByFirstname(keyword);
    }

    public UserDetails loadUserByUsername(LoginReqDto loginReqDto) throws UsernameNotFoundException {
        log.info("email: "+loginReqDto.toString());
        UserEntity userFromDB = userRepository.findByEmail(loginReqDto.getEmail());
//        boolean isPasswordMatch = passwordEncoder.matches(userEntity.getPassword(), userFromDB.getPassword());
//        log.info("userFromDB:   "+userFromDB.toString()+ "passwordmatch:  "+loginReqDto.getPassword().equals(userFromDB.getPassword()));
        if(userFromDB != null && loginReqDto.getPassword().equals(userFromDB.getPassword())) {
            return new User(userFromDB.getEmail(),
                    userFromDB.getPassword(), mapRolesToAuthorities(userFromDB.getRoles()));
        }

        throw new UsernameNotFoundException("Invalid username or password.");

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<UserRole> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toUpperCase())).collect(Collectors.toList());
    }
}
