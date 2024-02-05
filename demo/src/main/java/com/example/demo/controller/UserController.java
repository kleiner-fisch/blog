package com.example.demo.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/users")
public class UserController {

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;


    // TODO in the api.yaml openAPI spec file we use user_id, instead of userId
    //      We should fix the api.yaml names to use camel case.
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public Long createUser(@RequestBody User user){
        return this.userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public Long updateUser(@PathVariable("userId") Long userId, @RequestBody User user){
        return this.userService.updateUser(userId, user);
    }

    
    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId){
        return this.userService.getUser(userId);
    }

    // TODO the default value does not seem interpreted by openapi correctly...
    @Operation(description = "Returns a page of all users.")
    @GetMapping()
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "10") Optional<Integer> pageLimit, 
            @RequestParam(defaultValue = "0") Optional<Integer> pageOffset,
            @RequestParam(defaultValue = "userId") Optional<String> sortBy,
            @RequestParam(defaultValue = "asc") Optional<String> sortOrder){
        return this.userService.getAllUsers(pageLimit, pageOffset, sortBy, sortOrder);
    }

    @DeleteMapping("/{userId}")
    public Long deleteUser(@PathVariable("userId") Long userId){
        return this.userService.deleteUser(userId);
    }
}
