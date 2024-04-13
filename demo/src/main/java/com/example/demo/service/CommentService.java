package com.example.demo.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;

public interface CommentService {
    public Long createComment(CommentDTO comment, Long postId);

    public Long deleteComment(Long commentId);

    public Comment getComment(Long commentId);

    public Page<Comment> findAllCommentsForPost(Long postID, Pageable pageRequest);

    public void deleteAllComments();
        
}
