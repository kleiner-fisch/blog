package com.example.demo.model;

import java.util.Date;

public class Post {
    private Long id;
    private String title;
    private String content;
    private User author;
    private Date date;

    public Post(Long id, String title, String content, User author, Date date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
    }
    
    public Post() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

}
