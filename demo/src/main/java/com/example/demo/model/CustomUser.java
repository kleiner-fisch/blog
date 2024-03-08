package com.example.demo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.demo.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="User_table")
public class CustomUser {
    @Column(name="username", unique = true)
    private String username;
    @NotBlank(message = "password must not be empty")
    @Column(name="password", nullable = false)
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(name="mail")
    @Email()
    private String mail;
    @Id
    @Column(name="userID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;
    @Column(name="roles", nullable = false)
    // Assumes to be comma seperated list of roles i.e. "ADMIN,USER"
    private String roles;



    

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Post> posts;

    public CustomUser() {   }
    
    public CustomUser(Long userId, String username, String password, String mail, List<Post> posts) {
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
    /**
     * returns the roles of this user. 
     * An example is ADMIN,USER whch becomes a String array {ADMIN, USER} 
     */
    public String[] getRoles(){
        return roles.split(UserService.SEPERATOR);
    }
    public void setRoles(String roles) {
        this.roles = roles;
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
        if(other instanceof CustomUser){
            CustomUser otherUser = (CustomUser) other;
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
