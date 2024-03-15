package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CustomUser;

public interface  UserRepository extends JpaRepository<CustomUser, Long>{
    Optional<CustomUser> findByUsername(String username);
}
