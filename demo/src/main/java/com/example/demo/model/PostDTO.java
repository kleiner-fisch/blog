package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;


public class PostDTO extends RepresentationModel<PostDTO>{

    private Long postId;

    @NotNull(message = "title must not be null")
    private String title;

    @NotNull(message = "content must not be null")
    private String content;

    @JsonProperty( value = "author", access = JsonProperty.Access.READ_ONLY)
    private UserDTO author;


    @JsonProperty( value = "date", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime date;

    @JsonIgnore
    // @JsonProperty( value = "comments", access = JsonProperty.Access.READ_ONLY)
    private List<Comment> comments;

    public PostDTO(Long postId, String title, String content, CustomUser author, LocalDateTime date, List<Comment> comments) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.author = new UserDTO(author);
        this.date = date;
        this.comments = comments;
    }

    public PostDTO(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = new UserDTO(post.getAuthor());
        this.date = post.getDate();
        this.comments = post.getComments();

    }

    
    public PostDTO() {}

    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long id) {
        this.postId = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public UserDTO getAuthor() {
        return author;
    }
    public void setAuthor(UserDTO author) {
        this.author = author;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString(){
        String result = "postID: " + this.postId + " , userID: " + this.getAuthor().getUserId() +
               ", title: " + this.title + " , content: " + this.content + " , date: " + this.date;
        return result;
    }


    public List<Comment> getComments() {
        return comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    }