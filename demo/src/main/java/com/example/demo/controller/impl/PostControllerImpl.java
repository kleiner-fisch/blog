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

import com.example.demo.controller.PostController;
import com.example.demo.model.Post;
import com.example.demo.model.PostDTO;
import com.example.demo.model.PostDTOAssembler;
import com.example.demo.service.PostService;

@RestController
@Validated
public class PostControllerImpl implements PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostDTOAssembler postDTOAssembler;

    @Autowired
	private PagedResourcesAssembler<Post> pagedResourcesAssembler;


    @Override
    public Long createPost(PostDTO post){
        return this.postService.createPost(post);
    }


    @Override
    public PagedModel<PostDTO> getUserPosts(Long userId, Pageable pageable) {
        Page<Post> userPosts = postService.findAllPostsByUser(userId, pageable);
        // link to this method
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class).getUserPosts(userId, pageable)).withSelfRel();
       PagedModel<PostDTO> result = pagedResourcesAssembler.toModel(userPosts, postDTOAssembler, selfLink);
        return result;
    }



    @Override
    public PostDTO getPost(Long postId){
        Post post = this.postService.getPost(postId);
        return postDTOAssembler.toModel(post);
    }

    
    @Override
    public Long deletePost(Long postId){
        return this.postService.deletePost(postId);
    }

    @Override
    public PagedModel<PostDTO> getAllPosts(Pageable pageable) {
        Page<Post> posts = this.postService.getAllPosts(pageable);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class).getAllPosts(pageable)).withSelfRel();
        PagedModel<PostDTO> result = pagedResourcesAssembler.toModel(posts, postDTOAssembler, selfLink);
        return result;
    }


}
