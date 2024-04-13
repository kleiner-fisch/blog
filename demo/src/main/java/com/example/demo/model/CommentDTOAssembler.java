package com.example.demo.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.demo.controller.CommentController;
import com.example.demo.controller.PostController;

@Component
public class CommentDTOAssembler 
  extends RepresentationModelAssemblerSupport<Comment, CommentDTO> {

    public CommentDTOAssembler() {
        super(CommentController.class, CommentDTO.class);
    }

    @Override
    public CommentDTO toModel(Comment entity) {
        CommentDTO result = new CommentDTO(entity);
        Long postID = entity.getPost().getPostId();
        Link selfLink = WebMvcLinkBuilder.linkTo(getControllerClass(), postID)
                .slash(result.getCommentId()).withSelfRel();
        Link postLink = WebMvcLinkBuilder.linkTo(PostController.class)
                .slash(postID).withRel("post");
        result.add(postLink, selfLink);
        return result;
    }
}