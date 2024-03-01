package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CustomUser;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // TODO in the api.yaml openAPI spec file we use user_id, instead of userId
    //      We should fix the api.yaml names to use camel case.
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public Long createUser(@Valid() @RequestBody CustomUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public Long updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody CustomUser user){
        return this.userService.updateUser(userId, user);
    }

    
    @GetMapping("/{userId}")
    public CustomUser getUser(@PathVariable("userId") Long userId){
        return this.userService.getUser(userId);
    }

    // TODO the default value does not seem interpreted by openapi correctly...
    @Operation(description = "Returns a page of all users.")
    @GetMapping()
    public Page<CustomUser> getAllUsers(
            @RequestParam(name = "pageLimit", defaultValue = "10", required = false) @PositiveOrZero() Integer pageLimit, 
             @RequestParam(name = "pageOffset", defaultValue = "0", required = false) @PositiveOrZero Integer pageOffset,
             @RequestParam(name = "sortBy", defaultValue = "userId", required = false)  @Pattern(regexp = "userId|username|mail|") String sortBy,
           @RequestParam(name = "sortOrder", defaultValue = "asc", required = false)  @Pattern(regexp = "asc|desc")  String sortOrder){
        return this.userService.getAllUsers(Optional.ofNullable(pageLimit), 
                    Optional.ofNullable(pageOffset), Optional.ofNullable(sortBy), Optional.ofNullable(sortOrder));
    }

    @DeleteMapping("/{userId}")
    public Long deleteUser(@PathVariable("userId") Long userId){
        return this.userService.deleteUser(userId);
    }
}
