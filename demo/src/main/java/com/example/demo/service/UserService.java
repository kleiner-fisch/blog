package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.example.demo.model.CustomUser;

import jakarta.validation.constraints.Pattern;

public interface UserService {


    public Long createUser(CustomUser user);
    public Long updateUser(Long userId, CustomUser user);
    /**
     * Deleted the user with the given ID. 
     * The posts of the user will not be deleted. Instead a special 'deletedUser' is set as author.
     * 
     * The special 'deletedUser' cannot be deleted. If attempted, it responds with 'user not found'.
     */
    public Long deleteUser(Long userId);
    public CustomUser getUser(Long userId);
    public Page<CustomUser> getAllUsers();
    public Page<CustomUser> getAllUsers(Optional<Integer> pageLimit, Optional<Integer> pageOffset, Optional<String> sortDirection,
            Optional<String> SortBy);

    public void addAllUsers(List<CustomUser> users);

    public boolean userExists(String username);

    public void deleteAllUsers();

    public void flush();


}
