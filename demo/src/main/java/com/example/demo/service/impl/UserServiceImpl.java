package com.example.demo.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.persistence.EntityNotFoundException;

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
    public Long updateUser(Long userId, User user) {
        try {
            User reference = this.userRepository.getReferenceById(userId);
            reference.setMail(user.getMail());
            reference.setPassword(user.getPassword());
            reference.setUsername(user.getUsername());
            this.userRepository.save(reference);
            return reference.getUserId();
        } catch (EntityNotFoundException e) {
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }

    @Override
    public Long deleteUser(Long userId) {
        if(this.userRepository.existsById(userId)){
            this.userRepository.deleteById(userId);
            return userId;
        }else{
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }

    @Override
    public User getUser(Long userId) {
        var user = this.userRepository.findById(userId);
        if (user.isPresent()){
            return user.get();
        }else {
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();

    }
}
