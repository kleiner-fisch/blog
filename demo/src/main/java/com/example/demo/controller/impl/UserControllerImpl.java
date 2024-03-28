package com.example.demo.controller.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.service.DefaultValues.USER_ROLE;
import static com.example.demo.service.DefaultValues.getDefaultUserPageRequest;


import com.example.demo.controller.PostController;
import com.example.demo.controller.UserController;
import com.example.demo.service.UserDTO;
import com.example.demo.service.UserService;

@RestController
@Validated
public class UserControllerImpl implements UserController{

    // TODO in the api.yaml openAPI spec file we use user_id, instead of userId
    // We should fix the api.yaml names to use camel case.
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public UserControllerImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
        
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
    public CollectionModel<UserDTO> getAllUsers(Pageable pageable){
        Page<UserDTO> users =  userService.getAllUsers(pageable);
        users.forEach(user -> user.add(WebMvcLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).withRel("user")));
        CollectionModel<UserDTO> result = CollectionModel.of(users).withFallbackType(UserDTO.class);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers(pageable)).withSelfRel();
        result.add(link);
        return result;
      }
      
   
    // @Override
    // public Page<UserDTO> getAllUsers(Integer pageSize, Integer page, 
    //         String sortDirection, String sortBy) {
    //     Page<UserDTO> users = this.userService.getAllUsers(Optional.ofNullable(pageSize),
    //             Optional.ofNullable(page), Optional.ofNullable(sortDirection), Optional.ofNullable(sortBy));
    //     // we do not want the posts when listing many users
    //     users.forEach(user -> user.setPosts(null));
    //     return users;
    // }

    @Override
    public Long deleteUser(Long userId) {
        return this.userService.deleteUser(userId);
    }
}
