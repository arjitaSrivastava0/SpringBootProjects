package com.student.management.entity;


import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
@Table(name = "role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long role_id;
    private String name;

    public UserRole(){}
    public UserRole(String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return role_id;
    }
    public void setId(Long id) {
        this.role_id = role_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}


