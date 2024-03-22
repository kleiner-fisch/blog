package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;

public interface  CommentRepository extends JpaRepository<Comment, Long>{
    Page<Comment> findAllByPost(Post post, Pageable pageRequest);

}
