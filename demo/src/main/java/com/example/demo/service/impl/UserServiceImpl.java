package com.example.demo.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.example.demo.Util;
import com.example.demo.exception.NotAuthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserDTO;
import com.example.demo.service.UserService;

import jakarta.persistence.EntityNotFoundException;

import static com.example.demo.service.DefaultValues.DELETED_USER;
import static com.example.demo.service.DefaultValues.ADMIN_ROLE;
import static com.example.demo.service.DefaultValues.DEFAULT_SORTING_DIRECTION;
import static com.example.demo.service.DefaultValues.DEFAULT_USER_SORTING_COLUMN;


@Service
public class UserServiceImpl implements UserService{

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    private UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Long createUser(UserDTO user) {
        CustomUser userEntity = new CustomUser(user);
        this.userRepository.save(userEntity);
        return user.getUserId();
    }


    private boolean currentSessionMayModify(String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User  user = (User) auth.getPrincipal();
        return user.getUsername().equals(username) || user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ADMIN_ROLE));
    }


    @Override
    public Long updateUser(Long userId, UserDTO user) {
        try {
            CustomUser storedUser = this.userRepository.getReferenceById(userId);
            Util.updateValue(storedUser::setMail, user.getMail());
            Util.updateValue(storedUser::setUsername, user.getUsername());
            Util.updateValue(storedUser::setPassword, user.getPassword());
            this.userRepository.save(storedUser);
            return storedUser.getUserId();
        } catch (EntityNotFoundException e) {
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }

    private void validateDeleteRequest(Long toDeleteID){
        if(!this.userRepository.existsById(toDeleteID)){
            String msg = "Requested user not found. Given userID: " + toDeleteID;
            throw new UserNotFoundException(msg);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User  currentUser = (User) auth.getPrincipal();
        CustomUser toDeleteUser = this.getUserEntity(toDeleteID);
        if(!currentSessionMayModify(toDeleteUser.getUsername())){
            throw new NotAuthorizedException("User with username " + currentUser.getUsername() + " is not authorized to delete user with ID " + toDeleteID);
        }
        if(toDeleteUser.getUsername().equals(DELETED_USER)){
            throw new NotAuthorizedException("user with username " + DELETED_USER + " cannot be deleted" );
        }

    }

    // TODO This behavior here is something we could document using swagger
    @Override
    public Long deleteUser(Long userId) {
        validateDeleteRequest(userId);
        CustomUser toDelete = this.getUserEntity(userId);
        // ensure we don't delete the special delete user
        CustomUser specialUser = this.userRepository.findByUsername(DELETED_USER).get();
        var iterator = toDelete.getPosts().iterator();
        while(iterator.hasNext()){
            Post post = iterator.next();
            post.setAuthor(specialUser);
            specialUser.getPosts().add(post);
            iterator.remove();
        }
        this.userRepository.delete(toDelete);
        return userId;
    }

    @Override
    public boolean userExists(String username){
        return this.userRepository.findByUsername(username).isPresent();
    }
    
    @Override
    public CustomUser getUserEntity(Long userId) {
        var user = this.userRepository.findById(userId);
        if (user.isPresent()){
            return user.get();
        }else {
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }

    @Override
    public UserDTO getUserDTO(Long userId) {
        var user = this.userRepository.findById(userId);
        if (user.isPresent()){
            return new UserDTO(user.get());
        }else {
            String msg = "Requested user not found. Given userID: " + userId;
            throw new UserNotFoundException(msg);
        }
    }


            
    @Override
    public Page<UserDTO> getAllUsers(
            Optional<Integer> pageLimit,
            Optional<Integer> pageOffset,
            Optional<String> sortDirection,
            Optional<String> sortBy) {
        Integer offset = pageOffset.orElseGet(() -> DEFAULT_PAGE_OFFSET);
        Integer limit = pageLimit.orElseGet(() -> DEFAULT_PAGE_LIMIT);

        Sort.Direction sortDirectionTmp = Sort.Direction.fromString(sortDirection.orElseGet(() -> DEFAULT_SORTING_DIRECTION));
        Sort.Order sortOder = Sort.Order.by(DEFAULT_USER_SORTING_COLUMN).with(sortDirectionTmp).ignoreCase();
        Sort sort = Sort.by(sortOder);
        var pageRequest = PageRequest.of(offset, limit, sort);

        Page<CustomUser> users = this.userRepository.findAll(pageRequest);
        return users.map(user -> new UserDTO(user));
    }


    @Override
    public Page<UserDTO> getAllUsers() {
        return this.getAllUsers(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }


    @Override
    public void addAllUsers(List<CustomUser> users) {
        // this.userRepository.saveAll(users.stream().map(user -> new CustomUser(user)).toList());
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
