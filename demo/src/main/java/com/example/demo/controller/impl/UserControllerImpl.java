package com.example.demo.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return this.userService.getUserDTO(userId);
    }
    
    @Override
    public Page<UserDTO> getAllUsers2(Pageable pageable){
        return userService.getAllUsers(pageable);
      }
      
   
    @Override
    public Page<UserDTO> getAllUsers(Integer pageSize, Integer page, 
            String sortDirection, String sortBy) {
        Page<UserDTO> users = this.userService.getAllUsers(Optional.ofNullable(pageSize),
                Optional.ofNullable(page), Optional.ofNullable(sortDirection), Optional.ofNullable(sortBy));
        // we do not want the posts when listing many users
        users.forEach(user -> user.setPosts(null));
        return users;
    }

    @Override
    public Long deleteUser(Long userId) {
        return this.userService.deleteUser(userId);
    }
}
