package com.example.demo.config;

import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
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
            User deleteUser = new User();
            deleteUser.setMail("deleted");
            deleteUser.setPassword("pw");
            deleteUser.setPosts(Collections.emptyList());
            deleteUser.setUsername(UserService.DELETED_USER);
            userService.createUser(deleteUser);
        }
    }
    
}
