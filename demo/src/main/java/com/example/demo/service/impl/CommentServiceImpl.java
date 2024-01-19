package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.model.Comment;
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
    public Long createComment(Comment comment, Long postId) {
        try {
        Post reference = this.postRepository.getReferenceById(postId);
        comment.setPost(reference);
        comment.setDate(LocalDateTime.now());
        reference.getComments().add(comment);
        this.commentRepository.save(comment);
        this.postRepository.save(reference);
        return comment.getCommentId();
    } catch (EntityNotFoundException e) {
        String msg = "Cannot create comment becaue the specified post was not found. Given postID: " + postId;
        throw new CommentNotFoundException(msg);
    }
    }

    @Override
    public Long updateComment(Long commentId, Comment comment) {
        try {
            Comment reference = this.commentRepository.getReferenceById(commentId);
            reference.setAuthor(comment.getAuthor());
            reference.setContent(comment.getContent());
            reference.setDate(comment.getDate());
            this.commentRepository.save(reference);
            return reference.getCommentId();
        } catch (EntityNotFoundException e) {
            String msg = "Requested comment not found. Given commentID: " + commentId;
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
    public Page<Comment> getAllComments(
            Optional<Integer> pageLimit,
            Optional<Integer> pageOffset,
            Optional<String> sortBy,
            Optional<String> sortOrder) {
        Integer offset = pageOffset.orElseGet(() -> DEFAULT_PAGE_OFFSET);
        Integer limit = pageLimit.orElseGet(() -> DEFAULT_PAGE_LIMIT);
        String sortColumn = sortBy.orElseGet(() -> "commentId");
        String sortDirection = sortOrder.orElseGet(() -> "asc");
        var pageRequest = PageRequest.of(offset, limit,
                Direction.fromString(sortDirection), sortColumn);
        return this.commentRepository.findAll(pageRequest);
    }

    @Override
    public Page<Comment> getAllComments() {
        return this.getAllComments(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }
}
