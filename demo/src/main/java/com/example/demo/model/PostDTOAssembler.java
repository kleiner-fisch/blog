package com.example.demo.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.demo.controller.CommentController;
import com.example.demo.controller.PostController;

@Component
public class PostDTOAssembler 
  extends RepresentationModelAssemblerSupport<Post, PostDTO> {

    private UserDTOAssembler userDTOAssembler;

    public PostDTOAssembler(UserDTOAssembler userDTOAssembler) {
        super(PostController.class, PostDTO.class);
        this.userDTOAssembler = userDTOAssembler;
    }

    @Override
    public PostDTO toModel(Post entity) {
        PostDTO result = new PostDTO(entity);
        Long postID = entity.getPostId();
        UserDTO author = userDTOAssembler.toModel(entity.getAuthor());
        result.setAuthor(author);
        Link selfLink = WebMvcLinkBuilder.linkTo(getControllerClass()).slash(postID).withSelfRel();
        Link commentsLink = WebMvcLinkBuilder.linkTo(CommentController.class, postID)
                .withRel("comments");
        result.add(selfLink, commentsLink);
        return result;
    }
}