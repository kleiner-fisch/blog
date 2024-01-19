package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.demo.Util;
import com.example.demo.exception.PostNotFoundException;
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
            Post storedPost = this.postRepository.getReferenceById(postId);
            Util.updateValue(storedPost::setAuthor, post.getAuthor());            
            Util.updateValue(storedPost::setContent, post.getContent());
            Util.updateValue(storedPost::setTitle, post.getTitle());
            this.postRepository.save(storedPost);
            return storedPost.getPostId();
        } catch (EntityNotFoundException e) {
            String msg = "Requested post not found. Given postID: " + postId;
            throw new PostNotFoundException(msg);
        }
    }


    // TODO we should delete also all comments of this post
    @Override
    public Long deletePost(Long postId) {
        if (this.postRepository.existsById(postId)) {
            this.postRepository.deleteById(postId);
            return postId;
        } else {
            String msg = "Requested post not found. Given postID: " + postId;
            throw new PostNotFoundException(msg);
        }
    }

    @Override
    public Post getPost(Long postId) {
        var post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            return post.get();
        } else {
            String msg = "Requested post not found. Given postID: " + postId;
            throw new PostNotFoundException(msg);
        }
    }

    @Override
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

    @Override
    public Page<Post> getAllPosts() {
        return this.getAllPosts(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }
}
