package com.example.demo.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.example.demo.Util;
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
            User storedUser = this.userRepository.getReferenceById(userId);
            Util.updateValue(storedUser::setMail, storedUser.getMail());
            Util.updateValue(storedUser::setUsername, storedUser.getUsername());
            Util.updateValue(storedUser::setPassword, storedUser.getPassword());
            this.userRepository.save(storedUser);
            return storedUser.getUserId();
        } catch (EntityNotFoundException e) {
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }

    // TODO we should somehow indicate in the posts of this user that the author is deleted.
    //      Perhaps replace the author field of the post with "deleted user"?
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
    public Page<User> getAllUsers(Pageable request) {
        return this.userRepository.findAll(request);
    }
}
