package com.example.demo.service;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Post;

public interface PostService {
    public Long createPost(PostDTO post);
    public Long updatePost(Long postId, PostDTO post);
    public Long deletePost(Long postId);
    public PostDTO getPost(Long postId);

    public Page<PostDTO> getAllPosts(Pageable pageable);

    // public void addAllPosts(List<PostDTO> posts);

    // public void deleteAllPosts();

    // public void flush();

    public Page<PostDTO> findAllPostsByUser(Long userID, Pageable pageRequest);

}
