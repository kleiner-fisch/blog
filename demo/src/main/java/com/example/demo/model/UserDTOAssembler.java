package com.example.demo.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.demo.controller.PostController;
import com.example.demo.controller.UserController;

import static com.example.demo.service.DefaultValues.getDefaultUserPageRequest;


@Component
public class UserDTOAssembler 
  extends RepresentationModelAssemblerSupport<CustomUser, UserDTO> {

    public UserDTOAssembler() {
        super(UserController.class, UserDTO.class);
    }

    @Override
    public UserDTO toModel(CustomUser userEntity) {
        UserDTO user = new UserDTO(userEntity);
        Link selfLink = WebMvcLinkBuilder.linkTo(getControllerClass())
                .slash(user.getUserId()).withSelfRel();
        Link postsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                .getUserPosts(user.getUserId(), getDefaultUserPageRequest()))
                .withRel("userPosts");
        user.add(postsLink, selfLink);
        return user;
    }
}