package com.student.management.entity;


import java.util.Collection;
import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String password;

    private String isadmin;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "role_id"))

    private Collection<UserRole> roles;

    public UserEntity(){}

    public UserEntity(String firstName, String lastName, String email, String password, String isadmin, Collection<UserRole> roles) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isadmin= isadmin;
        this.roles = roles;
    }
    public Long getId() {
        return user_id;
    }
    public void setId(Long id) {
        this.user_id = user_id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsadmin() {return isadmin;}

    public void setIsadmin(String isadmin) {  this.isadmin = isadmin; }

    public Collection<UserRole> getRoles() {
        return roles;
    }
    public void setRoles(Collection<UserRole> roles) {
        this.roles = roles;
    }

}