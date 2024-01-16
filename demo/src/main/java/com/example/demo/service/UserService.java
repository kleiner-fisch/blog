package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;

public interface UserService {
    public Long createUser(User user);
    public Long updateUser(User user);
    public Long deleteUser(Long userId);
    public User getUser(Long userId);
    public List<User> getAllUsers();

}
