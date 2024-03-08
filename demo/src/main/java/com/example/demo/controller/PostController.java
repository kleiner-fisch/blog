package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {


    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // @GetMapping("/home")
    // public String registerForm(Model model){
    //     final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
    //     model.addAttribute("username", currentUserName);
    //     return "home";
    // }

    @RequestMapping("/home")
    public String getAllPosts(Model model, 
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortOrder) {
        Page<Post> pagePosts = this.postService.getAllPosts(size, page, sortBy, sortOrder);
        model.addAttribute("pagePosts", pagePosts);
        model.addAttribute("hasNextPage", pagePosts.hasNext());
        if(pagePosts.hasNext()){
             model.addAttribute("finalPage", pagePosts.getTotalPages());
        }
        model.addAttribute("hasPreviousPage", pagePosts.hasPrevious());
        model.addAttribute("pageNumber", pagePosts.getNumber());


        final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", currentUserName);

        return "home";
    }

    @PostMapping()
    public Long createPost(@Valid @RequestBody Post post){
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


    @GetMapping()
    public Page<Post> getAllPosts(@RequestParam Optional<Integer> pageLimit,
            @RequestParam Optional<Integer> pageOffset,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortOrder) {
        return this.postService.getAllPosts(pageLimit, pageOffset, sortBy, sortOrder);
    }



    @DeleteMapping("/{postId}")
    public Long deletePost(@PathVariable("postId") Long postId){
        return this.postService.deletePost(postId);
    }

}
