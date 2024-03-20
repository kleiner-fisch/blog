package com.example.demo.service;

import java.util.Arrays;
import java.util.List;

import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    
    @NotBlank(message = "username must not be null or only whitespace")
    @Schema(description = "username that is used to login, and is used as that users name in posts", example = "barman") 
    private String username;

    /**
     * The password should not be transferred to the client, but it should be transferred from client to server
     */
    @NotBlank(message = "password must not be empty")
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "password used for authentification", example = "12345", accessMode = AccessMode.WRITE_ONLY) 
    private String password;

    @Email(message = "given email address is not wellformed")
    @Schema(description = "mail account. Currently not used.", example = "barman@mail.org") 
    private String mail;


    /**
     * The id should not be read only. It should not be transferred within user objects
     */
    @Schema(description = "used as ID in the database, and also as parameter in URLs.", example = "1", accessMode = AccessMode.READ_ONLY) 
    private Long userId;

    /**
     * The roles should not be returned to the client, and should not be provided when creating new users. 
     */
    @JsonProperty( value = "roles", access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "used to check whether a user is authorized to perform actions") 
    @JsonIgnore
    private String roles;

    @JsonIgnore
    private List<Post> posts;

    public UserDTO() {   }
    
    public UserDTO(CustomUser user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.mail = user.getMail();
        this.posts = user.getPosts();
    }

    public UserDTO(Long userId, String username, String password, String mail, String roles, List<Post> posts) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.posts = posts;
        this.roles = roles;
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

    public void setRoles(String roles) {
        this.roles = roles;
    }
    public String getRoles() {
        if (roles == null){
            throw new RuntimeException("didnt expect call to getRoles when no roles are set, sorry");
    }
        return roles;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof UserDTO){
            UserDTO otherUser = (UserDTO) other;
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