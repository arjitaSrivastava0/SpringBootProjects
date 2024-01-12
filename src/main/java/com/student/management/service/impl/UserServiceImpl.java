package com.student.management.service.impl;

import com.student.management.Dto.LoginReqDto;
import com.student.management.Dto.UserRegistrationDto;
import com.student.management.entity.UserEntity;
import com.student.management.entity.UserRole;
import com.student.management.repository.UserRepo;
import com.student.management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepo userRepository;

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
        return userRepository.save(user);
    }

    @Override
    public UserEntity getByEmailId(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDetails loadUserByUsername(LoginReqDto loginReqDto) throws UsernameNotFoundException {
        log.info("email: "+loginReqDto.toString());
        UserEntity userFromDB = userRepository.findByEmail(loginReqDto.getEmail());
//        boolean isPasswordMatch = passwordEncoder.matches(userEntity.getPassword(), userFromDB.getPassword());
        log.info("userFromDB:   "+userFromDB.toString()+ "passwordmatch:  "+loginReqDto.getPassword().equals(userFromDB.getPassword()));
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
