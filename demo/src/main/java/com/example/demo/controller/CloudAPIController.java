package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/")
public class CloudAPIController {

    // TODO in the api.yaml openAPI spec file we use user_id, instead of userId
    //      We should fix the api.yaml names to use camel case.
    private UserService userService;

    public CloudAPIController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    public Long createUser(@RequestBody User user){
        return this.userService.createUser(user);
    }

    @PutMapping("users/{userId}")
    public Long updateUser(@PathVariable("userId") Long userId, @RequestBody User user){
        return this.userService.updateUser(userId, user);
    }

    @GetMapping("users/{userId}")
    public User getUser(@PathVariable("userId") Long userId){
        return this.userService.getUser(userId);
    }

    @GetMapping("users")
    public List<User> getAllUsers(){
        return this.userService.getAllUsers();
    }

    @DeleteMapping("users/{userId}")
    public Long deleteUser(@PathVariable("userId") Long userId){
        return this.userService.deleteUser(userId);
    }
}
