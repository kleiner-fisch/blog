package com.example.demo.controller.impl;

import java.util.function.Consumer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.CommentController;
import com.example.demo.controller.PostController;
import com.example.demo.controller.UserController;
import com.example.demo.model.Post;
import com.example.demo.service.PostDTO;
import com.example.demo.service.PostService;
import com.example.demo.service.UserDTO;
import com.example.demo.service.UserService;

@RestController
@Validated
public class PostControllerImpl implements PostController {


    private PostService postService;

    public PostControllerImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    public Long createPost(PostDTO post){
        return this.postService.createPost(post);
    }

    // @Override
    // public Long updatePost(Long postId, Post post){
    //     return this.postService.updatePost(postId, post);
    // }


    @Override
    public CollectionModel<PostDTO> getUserPosts(Long userId, Pageable pageable) {
        Page<PostDTO> userPosts = postService.findAllPostsByUser(userId, pageable);
        // Link to each post
        userPosts.forEach(post -> post.add(WebMvcLinkBuilder.linkTo(PostController.class).slash(post.getPostId()).withRel("post")));
        CollectionModel<PostDTO> result = CollectionModel.of(userPosts).withFallbackType(PostDTO.class);
        // link to this method
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class).getUserPosts(userId, pageable)).withSelfRel();
        result.add(link);
        // link to the user
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
        result.add(userLink);
        return result;
    }



    @Override
    public PostDTO getPost(Long postId){
        PostDTO post = this.postService.getPost(postId);
        Link selfLink = WebMvcLinkBuilder.linkTo(PostController.class).slash(postId).withSelfRel();
        post.add(selfLink);
        post.add(WebMvcLinkBuilder.linkTo(CommentController.class, postId).withRel("comments"));
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(post.getAuthor().getUserId()).withRel("author");
        post.add(userLink);
        return post;
    }

    
    @Override
    public Long deletePost(Long postId){
        return this.postService.deletePost(postId);
    }

    @Override
    public CollectionModel<PostDTO> getAllPosts(Pageable pageable) {
        Page<PostDTO> posts = this.postService.getAllPosts(pageable);
        Consumer<PostDTO> addPostLinks = post -> post.add(WebMvcLinkBuilder.linkTo(PostController.class)
                .slash(post.getPostId()).withRel("post"));
        // link to the users
        // Consumer<PostDTO> addUserLinks = post -> post.getAuthor().add(WebMvcLinkBuilder.linkTo(UserController.class)
        //         .slash(post.getAuthor().getUserId()).withRel("user"));

        // Link to each post
        posts.forEach(addPostLinks);
        CollectionModel<PostDTO> result = CollectionModel.of(posts).withFallbackType(PostDTO.class);
        // link to this method
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class).getAllPosts(pageable)).withSelfRel();
        result.add(link);

        return result;
    }


}
