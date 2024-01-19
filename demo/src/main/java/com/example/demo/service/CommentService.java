package com.example.demo.service;


import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.demo.model.Comment;

public interface CommentService {
    public Long createComment(Comment comment, Long postId);
    public Long updateComment(Long commentId, Comment comment);
    public Long deleteComment(Long commentId);
    public Comment getComment(Long commentId);
    public Page<Comment> getAllComments();
    public Page<Comment> getAllComments(Optional<Integer> pageLimit, Optional<Integer> pageOffset, Optional<String> sortBy,
            Optional<String> sortOrder);

}
