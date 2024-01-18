package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Long createPost(Post post) {
        post.setDate(LocalDateTime.now());
        this.postRepository.save(post);
        return post.getPostId();
    }

    @Override
    public Long updatePost(Long postId, Post post) {
        try {
            Post reference = this.postRepository.getReferenceById(postId);
            reference.setAuthor(post.getAuthor());
            reference.setContent(post.getContent());
            reference.setDate(post.getDate());
            reference.setTitle(post.getTitle());
            this.postRepository.save(reference);
            return reference.getPostId();
        } catch (EntityNotFoundException e) {
            String msg = "Requested post not found. Given postID: " + postId;
            throw new RuntimeException("not implemented");
        }
    }

    @Override
    public Long deletePost(Long postId) {
        if (this.postRepository.existsById(postId)) {
            this.postRepository.deleteById(postId);
            return postId;
        } else {
            String msg = "Requested post not found. Given postID: " + postId;
            throw new RuntimeException("not implemented");
        }
    }

    @Override
    public Post getPost(Long postId) {
        var post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            return post.get();
        } else {
            String msg = "Requested user not found. Given userID: " + postId;
            throw new RuntimeException("not implemented");
        }
    }

    public Page<Post> getAllPosts(
            Optional<Integer> pageLimit,
            Optional<Integer> pageOffset,
            Optional<String> sortBy,
            Optional<String> sortOrder) {
        Integer offset = pageOffset.orElseGet(() -> DEFAULT_PAGE_OFFSET);
        Integer limit = pageLimit.orElseGet(() -> DEFAULT_PAGE_LIMIT);
        String sortColumn = sortBy.orElseGet(() -> "postId");
        String sortDirection = sortOrder.orElseGet(() -> "asc");
        var pageRequest = PageRequest.of(offset, limit,
                Direction.fromString(sortDirection), sortColumn);
        return this.postRepository.findAll(pageRequest);
    }

    public Page<Post> getAllPosts() {
        return this.getAllPosts(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }
}