package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import com.example.demo.model.CustomUser;

public interface UserService {


    public static final String DELETED_USER = "deleted user";
    public static final String ADMIN = "admin";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String SEPERATOR = ",";

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
    public Page<CustomUser> getAllUsers(Optional<Integer> pageLimit, Optional<Integer> pageOffset, Optional<String> sortBy,
            Optional<String> sortOrder);

    public void addAllUsers(List<CustomUser> users);

    public boolean userExists(String username);

    public void deleteAllUsers();

    public void flush();

    public CustomUser getUserByUsername(String username);


}
