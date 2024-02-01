package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="User_Table")
public class User {
    @Column(name="username", unique = true)
    private String username;
    @Column(name="password")
    @JsonIgnore
    private String password;
    @Column(name="mail")
    private String mail;
    @Id
    @Column(name="userID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Post> posts;

    public User() {   }
    
    public User(Long userId, String username, String password, String mail, List<Post> posts) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

    @Override
    public boolean equals(Object other){
        if(other instanceof User){
            User otherUser = (User) other;
            boolean result = otherUser.getMail().equals(getMail());
            result = result && otherUser.getPassword().equals(getPassword());
            result = result && otherUser.getUsername().equals(getUsername());
            result = result && otherUser.getUserId().equals(getUserId());
            return result;
        }else{
            return false;
        }

    }

    @Override
    public String toString(){
        String result = "userID: " + this.userId + " , username: " + this.username +
               ", mail: " + this.mail;
        return result;
    }
}
