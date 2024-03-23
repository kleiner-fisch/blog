package com.example.demo.service;

import java.time.LocalDateTime;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CommentDTO {
    /**
     * Not only registered users, but anybody can comment. 
     * Hence, comment author is a simple string
     **/
    private String author;


    private Long commentId;

    private String content;

    private LocalDateTime date;

    @JsonIgnore
    // @JsonProperty( value = "post", access = JsonProperty.Access.READ_ONLY)
    private Post post;

    public CommentDTO() {}

    public CommentDTO(Comment comment) {
        this.author = comment.getAuthor();
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.date = comment.getDate();
        this.post = comment.getPost();
    }


    public CommentDTO(String author, Long commentId, String content, LocalDateTime date, Post post) {
        this.author = author;
        this.commentId = commentId;
        this.content = content;
        this.date = date;
        this.post = post;
    }


    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
