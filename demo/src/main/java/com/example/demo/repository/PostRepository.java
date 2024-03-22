package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;

public interface  PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findAllByAuthor(CustomUser author, Pageable pageRequest);

}
