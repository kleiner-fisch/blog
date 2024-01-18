package com.example.demo.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {


    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public Long createPost(@RequestBody Post post){
        return this.postService.createPost(post);
    }

    @PutMapping("/{postId}")
    public Long updatePost(@PathVariable("postId") Long postId, @RequestBody Post post){
        return this.postService.updatePost(postId, post);
    }

    @GetMapping("/{postId}")
    public Post getPost(@PathVariable("postId") Long postId){
        return this.postService.getPost(postId);
    }


    @DeleteMapping("/{postId}")
    public Long deletePost(@PathVariable("postId") Long postId){
        return this.postService.deletePost(postId);
    }

}
