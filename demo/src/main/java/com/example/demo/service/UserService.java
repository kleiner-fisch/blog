package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo.model.CustomUser;

import jakarta.validation.constraints.Pattern;

public interface UserService {


    public Long createUser(UserDTO user);
    public Long updateUser(Long userId, UserDTO user);
    /**
     * Deleted the user with the given ID. 
     * The posts of the user will not be deleted. Instead a special 'deletedUser' is set as author.
     * 
     * The special 'deletedUser' cannot be deleted. If attempted, it responds with 'user not found'.
     */
    public Long deleteUser(Long userId);

    /**
     * Returns a version of the requested user that is not managed by the database. 
     * As such, changes to the object will not be reflexted in the database 
     */
    public CustomUser getUserEntity(Long userId);

    public UserDTO getUserDTO(Long userId);
    public Page<UserDTO> getAllUsers();
    public Page<UserDTO> getAllUsers(Optional<Integer> pageLimit, Optional<Integer> pageOffset, Optional<String> sortDirection,
            Optional<String> SortBy);

    public Page<UserDTO> getAllUsers(Pageable pageable);

    public void addAllUsers(List<CustomUser> users);

    public boolean userExists(String username);

    public void deleteAllUsers();

    public void flush();


}
