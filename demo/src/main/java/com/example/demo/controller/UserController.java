package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/users")
public interface UserController {

    @Operation(description = "Creates a new user. Upon succesful user creation, the ID of the new user is returned")
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request."),
            @ApiResponse(responseCode = "400", description = "The user data is invalid. Either, a value is malformed (such as not a valid mail), or the username is already in use.") })
    public Long createUser(
            @Schema(description = "username, password and mail must be set and username must not already be used") 
                @Valid @RequestBody 
                UserDTO user);
    
}
