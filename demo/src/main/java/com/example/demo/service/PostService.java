package com.example.demo.service;


import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.demo.model.Post;

public interface PostService {
    public Long createPost(Post user);
    public Long updatePost(Long postId, Post post);
    public Long deletePost(Long postId);
    public Post getPost(Long postId);
    public Page<Post> getAllPosts();
    public Page<Post> getAllPosts(Optional<Integer> pageLimit, Optional<Integer> pageOffset, Optional<String> sortBy,
            Optional<String> sortOrder);

}
