package com.example.demo.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.exception.NotAuthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Post;
import com.example.demo.service.PostDTO;
import com.example.demo.service.validation.AllowSortFields;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/posts")
@Tag(name = "Post_Endpoints", description = "Methods to interact with posts")
public interface PostController {

    //
    // CREATE NEW POST
    //
    @SecurityRequirement(name = "BasicAuth")
    @Operation(description = "creates a new post")
    @PostMapping()
    @ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Authentification failure"),
        @ApiResponse(responseCode = "400", description = "Error in the provided post data"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    public Long createPost(
        @Parameter(description = "the post to create. No null values allowed", example = "{'content':'Post content', 'title':'Post title'}" ) 
            @Valid @RequestBody PostDTO post);

    //
    // UPDATE POST
    //
    // @Operation(description = "updates the specified post, overwriting stored data with" + 
    //         " provided non-null data. Null data is ignored.")
    // @PutMapping("/{postId}")
    // @ApiResponses(value = {
    //     @ApiResponse(responseCode = "404", description = "The given post does not exist"),
    //     @ApiResponse(responseCode = "400", description = "Error in the provided post"),
    //     @ApiResponse(responseCode = "200", description = "Successfull request") })
    // public Long updatePost(
    //     @Parameter(description = "the post to update") 
    //         @PathVariable("postId") Long postId, 
    //     @Parameter(description = "the post to create. No null values allowed", example = "{'content':'New post content'" ) 
    //         @RequestBody Post post);


    //
    // GET USER POSTS
    //
    @Operation(description = "Returns a page of this users posts (not including comments).")
    @GetMapping("/users/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "The given user does not exist"),
            @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
            @ApiResponse(responseCode = "200", description = "Successfull request") })
    public PagedModel<PostDTO> getUserPosts(
        @Parameter(description = "the user whose posts we should fetch") 
            @PathVariable("userId") Long userId,
        @AllowSortFields(value = {"postId", "date"})
            @ParameterObject @PageableDefault(sort = {"date"}, direction = Sort.Direction.DESC) Pageable pageable);


    //
    // GET POST
    //
    @Operation(description = "returns the specified post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "The given post does not exist"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    @GetMapping("/{postId}")
    public PostDTO getPost(
        @Parameter(description = "id of the post to fetch")
            @PathVariable("postId") Long postId);

    //
    // GET ALL POSTS
    //
    @Operation(description = "Returns a page of all posts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
            @ApiResponse(responseCode = "200", description = "Successfull request") })
    @GetMapping()
    public PagedModel<PostDTO> getAllPosts(
        @Parameter(description = "page data of the posts to fetch")
            @AllowSortFields(value = {"postId", "date", "author"})
            @ParameterObject @PageableDefault(sort = {"date"}, direction = Sort.Direction.DESC) Pageable pageable) ;

    //
    // DELETE POST
    //
    @SecurityRequirement(name = "BasicAuth")
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
        @Parameter(description = "id of the post to be deleted") 
            @PathVariable("postId") Long postId);

}
