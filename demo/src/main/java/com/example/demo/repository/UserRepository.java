package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CustomUser;

public interface  UserRepository extends JpaRepository<CustomUser, Long>{
    CustomUser findByUsername(String username);
}
