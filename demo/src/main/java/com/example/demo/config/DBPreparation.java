package com.example.demo.config;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;


import static com.example.demo.service.DefaultValues.DELETED_USER;
import static com.example.demo.service.DefaultValues.USER_ROLE;



import com.example.demo.model.CustomUser;
import com.example.demo.model.UserDTO;
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
        if(!userService.userExists(DELETED_USER)){
            UserDTO deleteUser = new UserDTO();
            deleteUser.setMail("deleted@mail.org");
            deleteUser.setPassword("pw");
            deleteUser.setRoles(USER_ROLE);
            deleteUser.setUsername(DELETED_USER);
            userService.createUser(deleteUser);
        }
    }
    
}
