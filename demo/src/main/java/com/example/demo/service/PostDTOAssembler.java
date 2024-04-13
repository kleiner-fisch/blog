package com.example.demo.service;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.demo.controller.PostController;
import com.example.demo.model.Post;

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
        UserDTO author = userDTOAssembler.toModel(entity.getAuthor());
        result.setAuthor(author);
        result.add(WebMvcLinkBuilder.linkTo(getControllerClass()).slash(result.getPostId()).withSelfRel());
        return result;
    }
}