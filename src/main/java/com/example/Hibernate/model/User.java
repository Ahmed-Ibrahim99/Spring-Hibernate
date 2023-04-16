package com.example.Hibernate.model;

import com.example.Hibernate.annotation.FieldAnnotation;
import com.example.Hibernate.annotation.TableAnnotation;
import com.example.Hibernate.utils.FieldTypes;

@TableAnnotation(name = "USERS")
public class User {
    @FieldAnnotation(name = "user_id", type = FieldTypes.longType, isPrimary = true)
    private Long id;
    @FieldAnnotation(name = "user_name")
    private String userName;
    @FieldAnnotation
    private String email;
    @FieldAnnotation(name = "password")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
