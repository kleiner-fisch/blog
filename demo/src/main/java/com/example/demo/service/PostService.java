package com.example.demo.service;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Post;

public interface PostService {
    public Long createPost(PostDTO post);
    public Long deletePost(Long postId);
    public Post getPost(Long postId);

    public Page<Post> getAllPosts(Pageable pageable);



    public Page<Post> findAllPostsByUser(Long userID, Pageable pageRequest);

}
