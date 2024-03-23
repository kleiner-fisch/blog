package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.demo.Util;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostDTO;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {

    public static final Integer DEFAULT_PAGE_LIMIT = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    private PostRepository postRepository;
        private UserService userService;


    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }



    @Override
    public Long createPost(PostDTO post) {
        Post postEntity = new Post(post);
        postEntity.setDate(LocalDateTime.now());
        CustomUser currentUser = userService.getCurrentUser();
        postEntity.setAuthor(currentUser);
        currentUser.getPosts().add(postEntity);
        this.postRepository.save(postEntity);
        return postEntity.getPostId();
    }

    @Override
    public Long updatePost(Long postId, PostDTO post) {
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
    public PostDTO getPost(Long postId) {
        var post = this.postRepository.findById(postId);
        if (post.isPresent()) {
            return new PostDTO(post.get());
        } else {
            String msg = "Requested post not found. Given postID: " + postId;
            throw new PostNotFoundException(msg);
        }
    }

    @Override
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return this.postRepository.findAll(pageable).map(post -> new PostDTO(post));
    }

    @Override
    public Page<PostDTO> findAllPostsByUser(Long userID, Pageable pageable) {
        CustomUser author = new CustomUser();
        author.setUserId(userID);
        return this.postRepository.findAllByAuthor(author, pageable).map(post -> new PostDTO(post));
    }

    // @Override
    // public Page<Post> getAllPosts(
    //         Optional<Integer> pageLimit,
    //         Optional<Integer> pageOffset,
    //         Optional<String> sortBy,
    //         Optional<String> sortOrder) {
    //     Integer offset = pageOffset.orElseGet(() -> DEFAULT_PAGE_OFFSET);
    //     Integer limit = pageLimit.orElseGet(() -> DEFAULT_PAGE_LIMIT);
    //     String sortColumn = sortBy.orElseGet(() -> "postId");
    //     String sortDirection = sortOrder.orElseGet(() -> "asc");
    //     var pageRequest = PageRequest.of(offset, limit,
    //             Direction.fromString(sortDirection), sortColumn);
    //     return this.postRepository.findAll(pageRequest);
    // }

    // @Override
    // public Page<Post> getAllPosts() {
    //     return this.getAllPosts(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    // }

    // @Override
    // public void addAllPosts(List<Post> users) {
    //     this.postRepository.saveAll(users);
    // }

    // @Override
    // public void deleteAllPosts(){
    //     this.postRepository.deleteAll();
    // }

    // @Override
    // public void flush(){
    //     this.postRepository.flush();
    // }
}
