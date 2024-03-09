package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.demo.model.Post;

public interface PostService {
    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;
    
    public Long createPost(Post post);
    public Long updatePost(Long postId, Post post);
    public Long deletePost(Long postId);
    public Post getPost(Long postId);
    public Page<Post> getAllPosts();
    public Page<Post> getAllPosts(Optional<Integer> pageLimit, Optional<Integer> pageOffset, Optional<String> sortBy,
            Optional<String> sortOrder);

    public void addAllPosts(List<Post> posts);

    public void deleteAllPosts();

    public void flush();
}
