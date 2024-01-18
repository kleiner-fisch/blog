package com.example.demo.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Post;
import com.example.demo.service.PostService;

@Controller
@RequestMapping("/")
public class HomeController {

    private PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    public String home(@RequestParam Optional<Integer> pageLimit,
            @RequestParam Optional<Integer> pageOffset,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortOrder,
            Model model) {
        Page<Post> posts = this.postService.getAllPosts(pageLimit, pageOffset, sortBy, sortOrder);
        model.addAttribute("posts", posts);
        return "home";
    }

    /*
     * public Page<Post> getAllPosts(
     * 
     * @RequestParam Optional<Integer> pageLimit,
     * 
     * @RequestParam Optional<Integer> pageOffset,
     * 
     * @RequestParam Optional<String> sortBy,
     * 
     * @RequestParam Optional<String> sortOrder){
     * return this.postService.getAllPosts(pageLimit, pageOffset, sortBy,
     * sortOrder);
     * }
     */

}
