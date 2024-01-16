package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="User_table")
public class User {
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="mail")
    private String mail;
    @Id
    @Column(name="userID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    public User() {   }
    
    public User(Long userId, String username, String password, String mail) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getMail() {
        return mail;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
