package com.example.demo.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.exception.NotAuthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserDTO;
import com.example.demo.service.validation.AllowSortFields;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import static com.example.demo.service.DefaultValues.DEFAULT_SORTING_DIRECTION;
import static com.example.demo.service.DefaultValues.USER_ROLE;
import static com.example.demo.service.DefaultValues.DEFAULT_USER_SORTING_COLUMN;

import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_LIMIT_STRING;
import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_OFFSET_STRING;

@RequestMapping("/users")
public interface UserController {

    //
    // CREATE USER
    //
    @Operation(description = "Creates a new user. Upon succesful user creation, the ID of the new user is returned")
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request."),
            @ApiResponse(responseCode = "400", description = "The user data is invalid. Either, a value is malformed (such as not a valid mail), or the username is already in use.") })
    public Long createUser(
            @Schema(description = "username, password and mail must be set and username must not already be used") 
                @Valid @RequestBody 
                UserDTO user);

    //
    // GET USER
    //
    @Operation(description = "Returns a single user.")
    @GetMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request"),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                    @Content(schema = @Schema(implementation = UserNotFoundException.class)) })
    })
    public UserDTO getUser(
            @Schema(description = "id of the user to be fetched", type = "long") @PathVariable("userId") Long userId);
    
    //
    // GET ALL USERS
    //
    @Operation(description = "Returns a page of all users.")
    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
            @ApiResponse(responseCode = "200", description = "Successfull request") })
    public Page<UserDTO> getAllUsers(
            @Schema(description = "number of users per page", required = false, type = "int", defaultValue = DEFAULT_PAGE_LIMIT_STRING) @RequestParam(name = "pageSize", defaultValue = "10", required = false) @PositiveOrZero() Integer pageSize,
            @Schema(description = "the page to fetch", required = false, type = "int", defaultValue = DEFAULT_PAGE_OFFSET_STRING) @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero Integer page,
            @Schema(description = "the direction of the users should be sorted", required = false, type = "string", allowableValues = {
                    "asc",
                    "desc" }, defaultValue = DEFAULT_SORTING_DIRECTION) @RequestParam(name = "sortDirection", defaultValue = "asc", required = false) @Pattern(regexp = "asc|desc", flags = {
                            Pattern.Flag.CASE_INSENSITIVE }) String sortDirection,
            @Schema(description = "the property used to sort the users", required = false, type = "string", allowableValues = {
                    "userId", "username",
                    "mail" }, defaultValue = DEFAULT_USER_SORTING_COLUMN) @RequestParam(name = "sortBy", defaultValue = DEFAULT_USER_SORTING_COLUMN, required = false) @Pattern(regexp = "userId|username|mail", flags = {
                            Pattern.Flag.CASE_INSENSITIVE }) String sortBy);

    // TEST METHOD
    @Operation(description = "Returns a page of all users.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    @GetMapping("/test")
    public Page<UserDTO> getAllUsers2(
            @AllowSortFields(value = {"username", "userId"})
            @ParameterObject @PageableDefault(sort = {"username"} ) Pageable pageable);

    // 
    // DELETE USER
    //
    @Operation(description = "Deletes the user with the given userId. May only be done by admins and the user to be removed itself. "
        + "When a user is deleted, the users posts are not deleted. Instead, all posts of the user are transferred to a special user")
    @DeleteMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request"),
            @ApiResponse(responseCode = "404", description = "user to delete not found", content = {
                    @Content(schema = @Schema(implementation = UserNotFoundException.class)) }),
            @ApiResponse(responseCode = "403", description = "not authorized to delete the given user", content = {
                    @Content(schema = @Schema(implementation = NotAuthorizedException.class)) }),
            @ApiResponse(responseCode = "401", description = "Authentification failure") })
    public Long deleteUser(
            @Schema(description = "id of the user to be deleted", type = "long") @PathVariable("userId") Long userId);
}
