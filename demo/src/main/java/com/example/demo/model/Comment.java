package com.example.demo.model;

import java.util.Date;

public class Comment {
    /**
     * Not only registered users, but anybody can comment. 
     * Hence, comment author is a simple string
     **/
    private String author;

    private Long commentID;
    private String content;

    private Date date;

    public Comment() {}

    public Comment(String author, Long commentID, String content, Date date) {
        this.author = author;
        this.commentID = commentID;
        this.content = content;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
