package com.example.demo.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Comment;

public interface CommentService {
    public Long createComment(Comment comment, Long postId);
    public Long updateComment(Long commentId, Comment comment);
    public Long deleteComment(Long commentId);
    public Comment getComment(Long commentId);

    public Page<Comment> findAllCommentsForPost(Long postID, Pageable pageRequest);

    public void deleteAllComments();
        
}
