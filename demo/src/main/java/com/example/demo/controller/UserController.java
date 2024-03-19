package com.example.demo.controller;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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


import static com.example.demo.service.DefaultValues.DEFAULT_SORTING_DIRECTION;
import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_LIMIT;
import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_OFFSET;
import static com.example.demo.service.DefaultValues.USER_ROLE;
import static com.example.demo.service.DefaultValues.DEFAULT_USER_SORTING_COLUMN;

import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_LIMIT_STRING;
import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_OFFSET_STRING;


import com.example.demo.model.CustomUser;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {




    // TODO in the api.yaml openAPI spec file we use user_id, instead of userId
    //      We should fix the api.yaml names to use camel case.
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(description = "Creates a new user.")
    @PostMapping()
    public Long createUser(
        @Parameter(description = "username, password and mail must be set and username must not already be used") @Valid @RequestBody CustomUser user){
        user.setRoles(USER_ROLE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userService.createUser(user);
    }

    /*
    @Operation(description = "Not yet implemented")
    @PutMapping("/{userId}")
    public Long updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody CustomUser user){

        
        throw new NotImplementedException();
        // TODO need to check if the user is actually allowed to modify thegiven user
        // return this.userService.updateUser(userId, user);
    } */

    
    @GetMapping("/{userId}")
    public CustomUser getUser(@PathVariable("userId") Long userId){
        return this.userService.getUser(userId);
    }



    // TODO the default value does not seem interpreted by openapi correctly...
    @Operation(description = "Returns a page of all users.")
    @GetMapping()
    public Page<CustomUser> getAllUsers(
            @Schema(description = "number of users per page", required = false, type = "int", defaultValue = DEFAULT_PAGE_LIMIT_STRING)
                    @RequestParam(name = "pageLimit", defaultValue = "10", required = false) 
                    @PositiveOrZero() Integer pageLimit, 
            @Schema(description = "the page to fetch", required = false, type = "int", defaultValue = DEFAULT_PAGE_OFFSET_STRING )
                    @RequestParam(name = "pageOffset", defaultValue = "0", required = false) 
                    @PositiveOrZero Integer pageOffset,
            @Schema(description = "the direction of the users should be sorted", required = false, type = "string", allowableValues = { "asc", "desc"}, defaultValue = DEFAULT_SORTING_DIRECTION)
                    @RequestParam(name = "sortDirection", defaultValue = "asc", required = false)  
                    @Pattern(regexp = "asc|desc")  
                    String sortDirection,
            @Schema(description = "the property used to sort the users", required = false, type = "string", allowableValues = { "userId", "username", "mail"}, defaultValue = DEFAULT_USER_SORTING_COLUMN )
                    @RequestParam(name = "sortBy", defaultValue = DEFAULT_USER_SORTING_COLUMN, required = false)  
                    @Pattern(regexp = "userId|username|mail") String sortBy){
        // TODO with hte default value in the RequestParam annotation, do I need the optional?? 
        
        return this.userService.getAllUsers(Optional.ofNullable(pageLimit), 
                    Optional.ofNullable(pageOffset), Optional.ofNullable(sortDirection), Optional.ofNullable(sortBy));
    }

    @Operation(description = "Deletes the user with the given userId. May only be done by admins and the user to be removed itself. " + 
        "When a user is deleted all posts of the user are transferred to a special user")
    @DeleteMapping("/{userId}")
    public Long deleteUser(@PathVariable("userId") Long userId){
        return this.userService.deleteUser(userId);
    }
}
