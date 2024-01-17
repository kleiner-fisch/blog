package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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

@RestController
@RequestMapping("/")
public class CloudAPIController {

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;


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
    public Page<User> getAllUsers(
            @RequestParam Optional<Integer> pageLimit, 
            @RequestParam Optional<Integer> pageOffset,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortOrder){
        Integer offset = pageOffset.orElseGet(() -> DEFAULT_PAGE_OFFSET);
        Integer limit = pageLimit.orElseGet(() -> DEFAULT_PAGE_LIMIT);
        String sortColumn = sortBy.orElseGet(() -> "userId");
        String sortDirection = sortOrder.orElseGet(() -> "asc");
        var pageRequest = PageRequest.of(offset, limit, 
            Direction.fromString(sortDirection), sortColumn);
        return this.userService.getAllUsers(pageRequest);
    }

    @DeleteMapping("users/{userId}")
    public Long deleteUser(@PathVariable("userId") Long userId){
        return this.userService.deleteUser(userId);
    }
}
