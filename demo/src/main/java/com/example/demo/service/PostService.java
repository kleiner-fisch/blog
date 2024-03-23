package com.example.demo.service;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Post;

public interface PostService {
    public Long createPost(Post post);
    public Long updatePost(Long postId, Post post);
    public Long deletePost(Long postId);
    public Post getPost(Long postId);

    public Page<Post> getAllPosts(Pageable pageable);

    public void addAllPosts(List<Post> posts);

    public void deleteAllPosts();

    public void flush();

    public Page<Post> findAllPostsByUser(Long userID, Pageable pageRequest);

}
