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
import com.example.demo.service.CommentDTO;
import com.example.demo.service.CommentService;

@RestController
@Validated
public class CommentControllerImpl implements CommentController{


    private CommentService commentService;

    public CommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    public Long createComment(Long postId, CommentDTO comment){
        return this.commentService.createComment(comment, postId);
    }

    // public Long updateComment(Long postId, Long commentId, CommentDTO comment){
    //     return this.commentService.updateComment(commentId, comment);
    // }

    public CommentDTO getComment(Long postId, Long commentId){
        CommentDTO comment = this.commentService.getComment(commentId);
        comment.setPost(null);
        // Link to the post this comment refers to
        Link postLink = WebMvcLinkBuilder.linkTo(PostController.class).slash(postId).withRel("post");
        comment.add(postLink);
        // Link to this comment
        Link selfLink = WebMvcLinkBuilder.linkTo(CommentController.class, postId).slash(commentId).withSelfRel();
        comment.add(selfLink);
        return comment;
    }


    public Long deleteComment(Long postId, Long commentId){
        return this.commentService.deleteComment(commentId);
    }

    @Override
    public CollectionModel<CommentDTO> getAllComments(Long postID, Pageable pageable) {
        Page<CommentDTO> comments =  this.commentService.findAllCommentsForPost(postID, pageable);
        // We don't want each comment to include the post
        comments.forEach(c -> c.setPost(null));
        CollectionModel<CommentDTO> result = CollectionModel.of(comments).withFallbackType(CommentDTO.class);
        // Link to this method/endpoint
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class, postID).getAllComments(postID, pageable)).withSelfRel();
        result.add(selfLink);
        // Link to each comment
        Consumer<CommentDTO> addPostLinks = comment -> comment.add(
                WebMvcLinkBuilder.linkTo(CommentController.class, postID).slash(comment.getCommentId()).withRel("comment"));
        comments.forEach(addPostLinks);
        // Link to the post these comments refer to
        Link postLink = WebMvcLinkBuilder.linkTo(PostController.class).slash(postID).withRel("post");
        result.add(postLink);
        return result;

    }

}
