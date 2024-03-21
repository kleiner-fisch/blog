package com.example.demo.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.service.DefaultValues.DEFAULT_SORTING_DIRECTION;
import static com.example.demo.service.DefaultValues.USER_ROLE;
import static com.example.demo.service.DefaultValues.DEFAULT_USER_SORTING_COLUMN;

import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_LIMIT_STRING;
import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_OFFSET_STRING;

import com.example.demo.controller.UserController;
import com.example.demo.exception.NotAuthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserDTO;
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

    @Operation(description = "Returns a single user.")
    @GetMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request"),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {
                    @Content(schema = @Schema(implementation = UserNotFoundException.class)) })
    })
    public UserDTO getUser(
            @Schema(description = "id of the user to be fetched", type = "long") @PathVariable("userId") Long userId) {
        return this.userService.getUserDTO(userId);
    }

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
                            Pattern.Flag.CASE_INSENSITIVE }) String sortBy) {
        Page<UserDTO> users = this.userService.getAllUsers(Optional.ofNullable(pageSize),
                Optional.ofNullable(page), Optional.ofNullable(sortDirection), Optional.ofNullable(sortBy));
        // we do not want the posts when listing many users
        users.forEach(user -> user.setPosts(null));
        return users;
    }

    @Operation(description = "Deletes the user with the given userId. May only be done by admins and the user to be removed itself. "
            +
            "When a user is deleted, the users posts are not deleted. Instead, all posts of the user are transferred to a special user")
    @DeleteMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request"),
            @ApiResponse(responseCode = "404", description = "user to delete not found", content = {
                    @Content(schema = @Schema(implementation = UserNotFoundException.class)) }),
            @ApiResponse(responseCode = "403", description = "not authorized to delete the given user", content = {
                    @Content(schema = @Schema(implementation = NotAuthorizedException.class)) }),
            @ApiResponse(responseCode = "401", description = "Authentification failure") })
    public Long deleteUser(
            @Schema(description = "id of the user to be deleted", type = "long") @PathVariable("userId") Long userId) {
        return this.userService.deleteUser(userId);
    }
}
