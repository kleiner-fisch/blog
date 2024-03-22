package com.example.demo.controller.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.example.demo.controller.CommentController;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;

@RestController
@Validated
public class CommentControllerImpl implements CommentController{


    private CommentService commentService;

    public CommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    public Long createComment(Long postId, Comment comment){
        return this.commentService.createComment(comment, postId);
    }

    public Long updateComment(Long postId, Long commentId, Comment comment){
        return this.commentService.updateComment(commentId, comment);
    }

    public Comment getComment(Long postId, Long commentId){
        return this.commentService.getComment(commentId);
    }


    public Long deleteComment(Long postId, Long commentId){
        return this.commentService.deleteComment(commentId);
    }

    @Override
    public Page<Comment> getAllComments(Long postID, Pageable pageable) {
        return this.commentService.findAllCommentsForPost(postID, pageable);

    }

}
