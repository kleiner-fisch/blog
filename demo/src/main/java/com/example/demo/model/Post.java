package com.example.demo.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Post_Table")
public class Post {
    @Column(name="postID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long postId;
    @Column(name="title")
    private String title;
    @Column(name="content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "userID")
    private User author;
    @Column(name="date")
    private LocalDateTime date;

    public Post(Long id, String title, String content, User author, LocalDateTime date) {
        this.postId = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
    }
    
    public Post() {}

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
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
