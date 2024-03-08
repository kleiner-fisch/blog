package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CustomUser;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Controller
//@Validated
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    // TODO in the api.yaml openAPI spec file we use user_id, instead of userId
    //      We should fix the api.yaml names to use camel case.


    @PostMapping("/register")
    public String createUser(@ModelAttribute("userForm") CustomUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(UserService.USER_ROLE);
        this.userService.createUser(user);
        return "index";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("userForm", new CustomUser());
        return "register";
    }

    // @GetMapping("/login")
    // public String loginForm(Model model){
    //     model.addAttribute("userForm", new CustomUser());
    //     return "login.html";
    // }

    // @PostMapping("/login")
    // public String login(@ModelAttribute("userForm") CustomUser user){
    //     return "login.html";
    // }
}
