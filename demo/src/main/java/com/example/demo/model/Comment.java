package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Comment_table")
public class Comment {
    /**
     * Not only registered users, but anybody can comment. 
     * Hence, comment author is a simple string
     **/
    @Column(name="author")
    private String author;

    @Column(name="commentID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long commentId;

    @Column(name="content")
    private String content;

    @Column(name="date")
    private LocalDateTime date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "postID", referencedColumnName = "postID")
    private Post post;

    public Comment() {}

    public Comment(String author, Long commentId, String content, LocalDateTime date, Post post) {
        this.author = author;
        this.commentId = commentId;
        this.content = content;
        this.date = date;
        this.post = post;
    }

    public Comment(CommentDTO comment) {
        this.author = comment.getAuthor();
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.date = comment.getDate();
        this.post = comment.getPost();
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
