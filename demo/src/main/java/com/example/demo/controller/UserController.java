package com.example.demo.controller;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
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

import com.example.demo.exception.NotAuthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.CustomUser;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Content;

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

    @Operation(description = "Creates a new user. Upon succesful user creation, the ID of the new user is returned")
    @PostMapping()
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfull request."), 
        @ApiResponse(responseCode = "400", description = "The user data is invalid. Either, a value is malformed (such as not a valid mail), or the username is already in use.")})
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

    @Operation(description = "Returns a single user.")
    @GetMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request"), 
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = { @Content(schema = @Schema(implementation = UserNotFoundException.class))} )
    })
    public CustomUser getUser(
        @Schema(description = "id of the user to be fetched", type = "long")
                @PathVariable("userId") Long userId){
        return this.userService.getUser(userId);
    }


    @Operation(description = "Returns a page of all users.")
    @GetMapping()
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
        @ApiResponse(responseCode = "200", description = "Successfull request")})
    public Page<CustomUser> getAllUsers(
            @Schema(description = "number of users per page", required = false, type = "int", defaultValue = DEFAULT_PAGE_LIMIT_STRING)
                    @RequestParam(name = "pageLimit", defaultValue = "10", required = false) 
                    @PositiveOrZero() Integer pageLimit, 
            @Schema(description = "the page to fetch", required = false, type = "int", defaultValue = DEFAULT_PAGE_OFFSET_STRING )
                    @RequestParam(name = "pageOffset", defaultValue = "0", required = false) 
                    @PositiveOrZero Integer pageOffset,
            @Schema(description = "the direction of the users should be sorted", required = false, type = "string", allowableValues = { "asc", "desc"}, defaultValue = DEFAULT_SORTING_DIRECTION)
                    @RequestParam(name = "sortDirection", defaultValue = "asc", required = false)  
                    @Pattern(regexp = "asc|desc", flags = {Pattern.Flag.CASE_INSENSITIVE})  
                    String sortDirection,
            @Schema(description = "the property used to sort the users", required = false, type = "string", allowableValues = { "userId", "username", "mail"}, defaultValue = DEFAULT_USER_SORTING_COLUMN )
                    @RequestParam(name = "sortBy", defaultValue = DEFAULT_USER_SORTING_COLUMN, required = false)  
                    @Pattern(regexp = "userId|username|mail", flags = {Pattern.Flag.CASE_INSENSITIVE}) String sortBy){        
        return this.userService.getAllUsers(Optional.ofNullable(pageLimit), 
                    Optional.ofNullable(pageOffset), Optional.ofNullable(sortDirection), Optional.ofNullable(sortBy));
    }

    @Operation(description = "Deletes the user with the given userId. May only be done by admins and the user to be removed itself. " + 
        "When a user is deleted, the users posts are not deleted. Instead, all posts of the user are transferred to a special user")
    @DeleteMapping("/{userId}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfull request"),
        @ApiResponse(responseCode = "404", description = "user to delete not found",
                content = { @Content(schema = @Schema(implementation = UserNotFoundException.class))}),
        @ApiResponse(responseCode = "403", description = "not authorized to delete the given user",
                content = { @Content(schema = @Schema(implementation = NotAuthorizedException.class))}),
        @ApiResponse(responseCode = "401", description = "Authentification failure")})
    public Long deleteUser(
        @Schema(description = "id of the user to be deleted", type = "long")
                @PathVariable("userId") Long userId){
        // TODO does user dleetion work??
        
        return this.userService.deleteUser(userId);
    }
}
