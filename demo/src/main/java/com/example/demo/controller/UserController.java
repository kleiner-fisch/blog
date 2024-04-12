package com.example.demo.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/users")
@Tag(name = "User_Endpoints", description = "Methods to interact with users")
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
            @Parameter(description = "username, password and mail must be set and username must not already be used") 
                @Valid @RequestBody 
                UserDTO user);

    //
    // GET USER
    //
    @Operation(description = "Returns a single user.")
    @GetMapping(value = "/{userId}", produces = MediaTypes.HAL_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request"),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                    @Content(schema = @Schema(implementation = UserNotFoundException.class)) })
    })
    public UserDTO getUser(
        @Parameter(description = "id of the user to fetch") 
            @PathVariable("userId") Long userId);
    
                //
    // GET ALL USERS
    //
    @Operation(description = "Returns a page of all users.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    @GetMapping()
    public PagedModel<UserDTO> getAllUsers(
        @AllowSortFields(value = {"username", "userId"})
            @ParameterObject @PageableDefault(sort = {"username"} ) 
            Pageable pageable);

            

    // 
    // DELETE USER
    //
    @SecurityRequirement(name = "BasicAuth")
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
        @Parameter(description = "id of the user to be deleted") 
            @PathVariable("userId") Long userId);
}
