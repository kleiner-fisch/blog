package com.example.demo.service.impl;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentDTO;
import com.example.demo.model.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.CommentService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentServiceImpl implements CommentService {

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Long createComment(CommentDTO comment, Long postId) {
        try {
            Post reference = this.postRepository.getReferenceById(postId);
            Comment commentEntity = new Comment(comment);
            commentEntity.setPost(reference);
            commentEntity.setDate(LocalDateTime.now());
            reference.getComments().add(commentEntity);
            this.commentRepository.save(commentEntity);
            this.postRepository.save(reference);
            return commentEntity.getCommentId();
        } catch (EntityNotFoundException e) {
            String msg = "Cannot create comment becaue the specified post was not found. Given postID: " + postId;
            throw new CommentNotFoundException(msg);
        }
    }


    @Override
    public Long deleteComment(Long commentId) {
        if (this.commentRepository.existsById(commentId)) {
            this.commentRepository.deleteById(commentId);
            return commentId;
        } else {
            String msg = "Requested comment not found. Given commentID: " + commentId;
            throw new CommentNotFoundException(msg);
        }
    }


    @Override
    public Comment getComment(Long commentId) {
        var comment = this.commentRepository.findById(commentId);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            String msg = "Requested comment not found. Given commentID: " + commentId;
            throw new CommentNotFoundException(msg);
        }
    }


    @Override
    public Page<Comment> findAllCommentsForPost(Long postID, Pageable pageRequest){
        Post post = new Post();
        post.setPostId(postID);
        return this.commentRepository.findAllByPost(post, pageRequest);
    }


    @Override
    public void deleteAllComments(){
        this.commentRepository.deleteAll();
    }
}

