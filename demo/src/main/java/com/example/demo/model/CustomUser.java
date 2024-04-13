package com.example.demo.model;

import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import static com.example.demo.service.DefaultValues.ROLE_SEPERATOR;;



@Entity
@Table(name="User_Table")
@Schema(description = "A user object that we use to authentificate users, and to track posts of users")
public class CustomUser {


    @Column(name="username", unique = true )
    @NotBlank(message = "username must not be null or only whitespace")
    @Schema(description = "username that is used to login, and is used as that users name in posts", example = "barman") 
    private String username;

    /**
     * The password should not be transferred to the client, but it should be transferred from client to server
     */
    @NotBlank(message = "password must not be empty")
    @Column(name="password", nullable = false)
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "password used for authentification", example = "12345", accessMode = AccessMode.WRITE_ONLY) 
    private String password;

    @Column(name="mail")
    @Email(message = "given email address is not wellformed")
    @Schema(description = "mail account. Currently not used.", example = "barman@mail.org") 
    private String mail;


    /**
     * The id should not be read only. It should not be transferred within user objects
     */
    @Id
    @Column(name="userID")
    @Schema(description = "used as ID in the database, and also as parameter in URLs.", example = "1", accessMode = AccessMode.READ_ONLY) 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    /**
     * The roles should not be returned to the client, and should not be provided when creating new users. 
     */
    @JsonProperty( value = "roles", access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "used to check whether a user is authorized to perform actions") 
    @JsonIgnore
    @Column(name="roles")
    private String roles;

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Post> posts;

    public CustomUser() {   }
    
    public CustomUser(UserDTO user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.mail = user.getMail();
        this.roles = user.getRoles();
    }

    public CustomUser(Long userId, String username, String password, String mail, String roles, List<Post> posts) {
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
    public String[] getRoles() {
        return roles.split(ROLE_SEPERATOR);
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
