package com.example.demo.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.CommentController;
import com.example.demo.controller.PostController;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.CommentDTOAssembler;
import com.example.demo.service.CommentService;

@RestController
@Validated
public class CommentControllerImpl implements CommentController{

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentDTOAssembler commentDTOAssembler;

    @Autowired
	private PagedResourcesAssembler<Comment> pagedResourcesAssembler;

    public Long createComment(Long postId, CommentDTO comment){
        return this.commentService.createComment(comment, postId);
    }


    public CommentDTO getComment(Long postId, Long commentId){
        Comment comment = this.commentService.getComment(commentId);
        CommentDTO result = commentDTOAssembler.toModel(comment);
        return result;
    }

    public Long deleteComment(Long postId, Long commentId){
        return this.commentService.deleteComment(commentId);
    }



    @Override
    public PagedModel<CommentDTO> getAllComments(Long postID, Pageable pageable) {
        Page<Comment> comments =  this.commentService.findAllCommentsForPost(postID, pageable);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class, postID)
                .getAllComments(postID, pageable)).withSelfRel();

        PagedModel<CommentDTO> result = pagedResourcesAssembler.toModel(comments, commentDTOAssembler, selfLink);

        // Link to the post these comments refer to
        Link postLink = WebMvcLinkBuilder.linkTo(PostController.class).slash(postID).withRel("post");
        result.add(postLink);
        return result;
    }

}
