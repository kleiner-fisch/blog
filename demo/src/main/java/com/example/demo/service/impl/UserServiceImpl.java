package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.example.demo.Util;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    private UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    // TODO does not check whether username already exists and exposes internal error to client
    @Override
    public Long createUser(CustomUser user) {
        this.userRepository.save(user);
        return user.getUserId();
    }

    @Override
    public Long updateUser(Long userId, CustomUser user) {
        try {
            CustomUser storedUser = this.userRepository.getReferenceById(userId);
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


    // TODO This behavior here is something we could document using swagger
    @Override
    public Long deleteUser(Long userId) {
        if(this.userRepository.existsById(userId)){
            CustomUser toDelete = this.userRepository.getReferenceById(userId);
            if(!toDelete.getUsername().equals(UserService.DELETED_USER)){
                CustomUser deletedUser = this.userRepository.findByUsername(UserService.DELETED_USER);
                var iterator = toDelete.getPosts().iterator();
                while(iterator.hasNext()){
                    Post post = iterator.next();
                    post.setAuthor(deletedUser);
                    deletedUser.getPosts().add(post);
                    iterator.remove();
                }
                this.userRepository.delete(toDelete);
                return userId;
            }
        }
        String msg = "Requested user not found. Given userID: " + userId;
        throw new UserNotFoundException(msg);
    }

    @Override
    public boolean userExists(String username){
        var user = Optional.ofNullable(this.userRepository.findByUsername(username));
        return user.isPresent();
    }
    

    @Override
    public CustomUser getUser(Long userId) {
        var user = this.userRepository.findById(userId);
        if (user.isPresent()){
            return user.get();
        }else {
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }

    @Override
    public Page<CustomUser> getAllUsers(
            Optional<Integer> pageLimit,
            Optional<Integer> pageOffset,
            Optional<String> sortBy,
            Optional<String> sortOrder) {
        Integer offset = pageOffset.orElseGet(() -> DEFAULT_PAGE_OFFSET);
        Integer limit = pageLimit.orElseGet(() -> DEFAULT_PAGE_LIMIT);
        String sortColumn = sortBy.orElseGet(() -> "userId");
        String sortDirection = sortOrder.orElseGet(() -> "asc");
        var pageRequest = PageRequest.of(offset, limit,
                Direction.fromString(sortDirection), sortColumn);
        return this.userRepository.findAll(pageRequest);
    }

    @Override
    public Page<CustomUser> getAllUsers() {
        return this.getAllUsers(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }


    @Override
    public void addAllUsers(List<CustomUser> users) {
        this.userRepository.saveAll(users);
    }
    
    @Override
    public void deleteAllUsers(){
        this.userRepository.deleteAll();
    }
    @Override
    public void flush(){
        this.userRepository.flush();
    }

}
