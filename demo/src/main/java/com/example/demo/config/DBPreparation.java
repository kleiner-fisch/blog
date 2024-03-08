package com.example.demo.config;

import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;

import com.example.demo.model.CustomUser;
import com.example.demo.service.UserService;

@DependsOnDatabaseInitialization
@Component
public class DBPreparation implements CommandLineRunner{

    private UserService userService;

    public DBPreparation(UserService userService) {
        this.userService = userService;

    }

    @Override
    public void run(String... args) throws Exception {
        addDeleteUser();

    }

    private void addDeleteUser() {
        if(!userService.userExists(UserService.DELETED_USER)){
            CustomUser deleteUser = new CustomUser();
            deleteUser.setMail("deleted@mail.org");
            deleteUser.setPassword("pw");
            deleteUser.setPosts(Collections.emptyList());
            deleteUser.setUsername(UserService.DELETED_USER);
            deleteUser.setRoles(UserService.USER_ROLE);
            userService.createUser(deleteUser);
        }
        if(!userService.userExists(UserService.ADMIN)){
            CustomUser deleteUser = new CustomUser();
            deleteUser.setMail("admin@mail.org");
            deleteUser.setPassword("pw");
            deleteUser.setPosts(Collections.emptyList());
            deleteUser.setUsername(UserService.ADMIN);
            deleteUser.setRoles(UserService.USER_ROLE+UserService.SEPERATOR+UserService.ADMIN_ROLE);
            userService.createUser(deleteUser);
        }
    }
    
}
