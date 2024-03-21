package com.example.demo.controller.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentControllerImpl {


    private CommentService commentService;

    public CommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public Long createComment(@PathVariable("postId") Long postId, @RequestBody Comment comment){
        return this.commentService.createComment(comment, postId);
    }

    @PutMapping("/{commentId}")
    public Long updateComment(@PathVariable("postId") Long postId, 
                @PathVariable("commentId") Long commentId,
                @RequestBody Comment comment){
        return this.commentService.updateComment(commentId, comment);
    }

    @GetMapping("/{commentId}")
    public Comment getComment(@PathVariable("postId") Long postId,
                @PathVariable("commentId") Long commentId){
        return this.commentService.getComment(commentId);
    }


    @GetMapping()
    public Page<Comment> getAllComments(@PathVariable("postId") Long commentId,
            @RequestParam Optional<Integer> pageLimit,
            @RequestParam Optional<Integer> pageOffset,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortOrder) {
        return this.commentService.getAllComments(pageLimit, pageOffset, sortBy, sortOrder);
    }



    @DeleteMapping("/{commentId}")
    public Long deleteComment(@PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId){
        return this.commentService.deleteComment(commentId);
    }

}
