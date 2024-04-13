package com.example.demo.service.impl;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.exception.PostNotFoundException;
import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;
import com.example.demo.model.PostDTO;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

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
    public Page<Post> getAllPosts(Pageable pageable) {
        Page<Post> posts = this.postRepository.findAll(pageable);
        return posts;
    }

    @Override
    public Page<Post> findAllPostsByUser(Long userID, Pageable pageable) {
        CustomUser author = new CustomUser();
        author.setUserId(userID);
        return this.postRepository.findAllByAuthor(author, pageable);
    }

}
