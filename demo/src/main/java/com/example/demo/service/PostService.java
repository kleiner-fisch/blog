package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.demo.model.Post;

public interface PostService {
    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    /**
     * Stores the given post to the data base. 
     * However, first sets the author to the currently logged in user and the date to todays date
     * 
     * Throws an error if there is no user currently logged in.
     * @param post the post to create
     * @return
     */
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
