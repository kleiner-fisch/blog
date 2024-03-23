package com.example.demo.controller.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.PostController;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

@RestController
@Validated
public class PostControllerImpl implements PostController {


    private PostService postService;
    private UserService userService;

    public PostControllerImpl(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public Long createPost(Post post){
        return this.postService.createPost(post);
    }

    // @Override
    // public Long updatePost(Long postId, Post post){
    //     return this.postService.updatePost(postId, post);
    // }


    @Override
    public Page<Post> getUserPosts(Long userId, Pageable pageable) {
        // TODO do we need PostDTO??
        return postService.findAllPostsByUser(userId, pageable);
        // UserDTO user =  this.userService.getUserDTO(userId);
        // return new PageImpl<>(user.getPosts(), pageable, user.getPosts().size());
    }




    @Override
    public Post getPost(Long postId){
        return this.postService.getPost(postId);
    }


    // @GetMapping()
    // public Page<Post> getAllPosts(@RequestParam Optional<Integer> pageLimit,
    //         @RequestParam Optional<Integer> pageOffset,
    //         @RequestParam Optional<String> sortBy,
    //         @RequestParam Optional<String> sortOrder) {
    //     return this.postService.getAllPosts(pageLimit, pageOffset, sortBy, sortOrder);
    // }

    @Override
    public Long deletePost(Long postId){
        return this.postService.deletePost(postId);
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return this.postService.getAllPosts(pageable);
    }

}
