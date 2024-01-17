package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.User;

public interface UserService {
    public Long createUser(User user);
    public Long updateUser(Long userId, User user);
    public Long deleteUser(Long userId);
    public User getUser(Long userId);
    public Page<User> getAllUsers(Pageable request);

}
