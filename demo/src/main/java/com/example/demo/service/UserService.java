package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import com.example.demo.model.User;

public interface UserService {


    public static String DELETED_USER = "deleted user";

    public Long createUser(User user);
    public Long updateUser(Long userId, User user);
    public Long deleteUser(Long userId);
    public User getUser(Long userId);
    public Page<User> getAllUsers();
    public Page<User> getAllUsers(Optional<Integer> pageLimit, Optional<Integer> pageOffset, Optional<String> sortBy,
            Optional<String> sortOrder);

    public void addAllUsers(List<User> users);

    public void deleteAllUsers();

    public void flush();


}
