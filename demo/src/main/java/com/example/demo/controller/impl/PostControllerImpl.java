package com.example.demo.controller.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.NotAuthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.CustomUser;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.UserDTO;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import static com.example.demo.service.DefaultValues.DEFAULT_SORTING_DIRECTION;
import static com.example.demo.service.DefaultValues.USER_ROLE;
import static com.example.demo.service.DefaultValues.DEFAULT_USER_SORTING_COLUMN;

import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_LIMIT_STRING;
import static com.example.demo.service.DefaultValues.DEFAULT_PAGE_OFFSET_STRING;

@RestController
@RequestMapping("/posts")
public class PostControllerImpl {


    private PostService postService;
    private UserService userService;

    public PostControllerImpl(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping()
    public Long createPost(@RequestBody Post post){
        return this.postService.createPost(post);
    }

    @PutMapping("/{postId}")
    public Long updatePost(@PathVariable("postId") Long postId, @RequestBody Post post){
        return this.postService.updatePost(postId, post);
    }


    @Operation(description = "Returns a page of this users posts (not including comments).")
    @GetMapping("/users/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "The given user does not exist"),
            @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
            @ApiResponse(responseCode = "200", description = "Successfull request") })
    public Page<Post> getUserPosts(
        @Schema(description = "the user whose posts we should fetch", required = true, type = "long") @PathVariable("userId") Long userId,
            @Schema(description = "number of posts per page", required = false, type = "int", defaultValue = DEFAULT_PAGE_LIMIT_STRING) @RequestParam(name = "pageSize", defaultValue = "10", required = false) @PositiveOrZero() Integer pageSize,
            @Schema(description = "the page to fetch", required = false, type = "int", defaultValue = DEFAULT_PAGE_OFFSET_STRING) @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero Integer page,
            @Schema(description = "the direction in which the posts should be sorted", required = false, type = "string", allowableValues = {
                    "asc",
                    "desc" }, defaultValue = DEFAULT_SORTING_DIRECTION) @RequestParam(name = "sortDirection", defaultValue = "asc", required = false) @Pattern(regexp = "asc|desc", flags = {
                            Pattern.Flag.CASE_INSENSITIVE }) String sortDirection) {
        Sort.Direction dir = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.DESC);
        UserDTO user =  this.userService.getUserDTO(userId);
        PageRequest posts = PageRequest.of(page, pageSize, dir, "date");
        return new PageImpl<>(user.getPosts(), posts, user.getPosts().size());

    }





    @GetMapping("/{postId}")
    public Post getPost(@PathVariable("postId") Long postId){
        return this.postService.getPost(postId);
    }


    @GetMapping()
    public Page<Post> getAllPosts(@RequestParam Optional<Integer> pageLimit,
            @RequestParam Optional<Integer> pageOffset,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortOrder) {
                // TODO I guess the sorting is not corretly passed on to the DB?
        return this.postService.getAllPosts(pageLimit, pageOffset, sortBy, sortOrder);
    }


    @Operation(description = "Deletes a post, along with its comments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfull request"),
            @ApiResponse(responseCode = "404", description = "post to delete not found", content = {
                    @Content(schema = @Schema(implementation = UserNotFoundException.class)) }),
            @ApiResponse(responseCode = "403", description = "not authorized to delete the given post", content = {
                    @Content(schema = @Schema(implementation = NotAuthorizedException.class)) }),
            @ApiResponse(responseCode = "401", description = "Authentification failure") })
    @DeleteMapping("/{postId}")
    public Long deletePost(
        @Schema(description = "id of the post to be deleted", type = "long") @PathVariable("postId") Long postId){
        return this.postService.deletePost(postId);
    }

}
