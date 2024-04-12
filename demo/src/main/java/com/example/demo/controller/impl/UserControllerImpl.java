package com.example.demo.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.service.DefaultValues.USER_ROLE;
import static com.example.demo.service.DefaultValues.getDefaultUserPageRequest;


import com.example.demo.controller.PostController;
import com.example.demo.controller.UserController;
import com.example.demo.model.CustomUser;
import com.example.demo.service.UserDTO;
import com.example.demo.service.UserDTOAssembler;
import com.example.demo.service.UserService;

@RestController
@Validated
public class UserControllerImpl implements UserController{

    // TODO in the api.yaml openAPI spec file we use user_id, instead of userId
    // We should fix the api.yaml names to use camel case.
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDTOAssembler userDTOAssembler;

    @Autowired
	private PagedResourcesAssembler<CustomUser> pagedResourcesAssembler;
        
    @Override
    public Long createUser(UserDTO user) {
        user.setRoles(USER_ROLE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userService.createUser(user);
    }

    /*
     * @Operation(description = "Not yet implemented")
     * 
     * @PutMapping("/{userId}")
     * public Long updateUser(@PathVariable("userId") Long
     * userId, @Valid @RequestBody CustomUser user){
     * 
     * 
     * throw new NotImplementedException();
     * // TODO need to check if the user is actually allowed to modify thegiven user
     * // return this.userService.updateUser(userId, user);
     * }
     */

    @Override
    public UserDTO getUser(Long userId) {

        UserDTO result = this.userService.getUserDTO(userId);
        Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(result.getUserId()).withSelfRel();
        result.add(selfLink);
        Link postsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class).getUserPosts(userId, getDefaultUserPageRequest())).withRel("userPosts");
        result.add(postsLink);
        return result;
    }

    @Override
    public PagedModel<UserDTO> getAllUsers(Pageable pageable){
        Page<CustomUser> users =  userService.getAllUsers(pageable);
        // CollectionModel<UserDTO> result = CollectionModel.of(users).withFallbackType(UserDTO.class);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers(pageable)).withSelfRel();
        return pagedResourcesAssembler.toModel(users, userDTOAssembler, selfLink);
      }

    @Override
    public Long deleteUser(Long userId) {
        return this.userService.deleteUser(userId);
    }
}
