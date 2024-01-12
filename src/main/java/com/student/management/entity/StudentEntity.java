package com.student.management.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Entity
@Table(name="students")
@Validated
@Data
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$",
            message = "firstname must be of 3 to 12 length with no special characters")
    @NotBlank(message = "Firstname is mandatory")
    @Column(name = "firstname", nullable = false)

    private String firstname;

    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$",
            message = "lastname must be of 3 to 12 length with no special characters")
    @NotBlank(message = "Lastname is mandatory")
    @Column(name = "lastname")
    private String lastname;

    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", nullable = false)
    @Email(regexp = "^(.+)@(.+)$", message = "Must be a well formed email.")
    private String email;

//    private List<CourseEntity> courses;

}

