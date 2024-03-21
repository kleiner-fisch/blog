package com.example.demo.controller.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.PostController;
import com.example.demo.exception.NotAuthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.UserDTO;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import static com.example.demo.service.DefaultValues.DEFAULT_SORTING_DIRECTION;
import static com.example.demo.service.DefaultValues.USER_ROLE;
import static com.example.demo.service.DefaultValues.DEFAULT_USER_SORTING_COLUMN;

import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_LIMIT_STRING;
import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_OFFSET_STRING;

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

    @Override
    public Long updatePost(Long postId, Post post){
        return this.postService.updatePost(postId, post);
    }


    @Override
    public Page<Post> getUserPosts(Long userId, Pageable pageable) {
        UserDTO user =  this.userService.getUserDTO(userId);
        return new PageImpl<>(user.getPosts(), pageable, user.getPosts().size());
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
