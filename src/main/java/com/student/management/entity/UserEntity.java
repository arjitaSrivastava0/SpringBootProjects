package com.student.management.entity;


import java.util.Collection;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
//@Table(name = "user")

@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userid;

    @Column(name = "firstname")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,12}$",
            message = "firstname must be of 3 to 12 length with no special characters")
    @NotBlank(message = "Firstname is mandatory")
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

    private String password;

    private String isadmin;



    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "userid"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "role_id"))

    private Collection<UserRole> roles;

    public UserEntity(){}

    public UserEntity(String firstname, String lastname, String email, String password, String isadmin, Collection<UserRole> roles) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.isadmin= isadmin;
        this.roles = roles;
    }


}