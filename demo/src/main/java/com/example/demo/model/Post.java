package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

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
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    @JsonProperty( value = "author", access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private CustomUser author;

    @Column(name="date")
    @JsonProperty( value = "date", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime date;

    @JsonProperty( value = "comments", access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Comment> comments;

    public Post(Long postId, String title, String content, CustomUser author, LocalDateTime date, List<Comment> comments) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.comments = comments;
    }


    public Post(PostDTO post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = new CustomUser(post.getAuthor());
        this.date = post.getDate();
        this.comments = post.getComments();
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
    public CustomUser getAuthor() {
        return author;
    }
    public void setAuthor(CustomUser author) {
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