package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Long createUser(User user) {
        this.userRepository.save(user);
        return user.getUserId();
    }

    @Override
    public Long updateUser(User user) {
        this.userRepository.save(user);
        return user.getUserId();
    }

    @Override
    public Long deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
        return userId;
    }

    @Override
    public User getUser(Long userId) {
        return this.userRepository.findById(userId).get();
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();

    }
    
}
