package com.example.demo.controller;

import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;
import com.example.demo.service.validation.AllowSortFields;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/posts/{postId}/comments")
public interface CommentController {

    //
    // CREATE COMMENT
    //
    @Operation(description = "creates a new comment for a specific post")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Error in the provided comment data in the request body"),
        @ApiResponse(responseCode = "404", description = "The specified post could not be found"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    @PostMapping()
    public Long createComment(
        @Parameter(description = "id of the post to which we wish to comment on")
            @PathVariable("postId") Long postId, 
        @Parameter(description = "the comment to create. No null values allowed", 
            example = "{'content':'Comment content', 'author':'author name'}" ) 
            @Valid @RequestBody Comment comment);

    // @PutMapping("/{commentId}")
    // public Long updateComment(
    //     @Parameter(description = "id of the post whose comment we wish to update")
    //             @PathVariable("postId") Long postId, 
    //     @Parameter(description = "id of the post to which we wish to comment on")
    //             @PathVariable("commentId") Long commentId,
    //             @RequestBody Comment comment);

    //
    // GET COMMENT
    //
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "The specified comment could not be found"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    @Operation(description = "returns a specific comment")
    @GetMapping("/{commentId}")
    public Comment getComment(
        @Parameter(description = "id of the post whose comment we wish to retrieve")
            @PathVariable("postId") Long postId,
        @Parameter(description = "id of the comment we wish to retrieve")
                @PathVariable("commentId") Long commentId);

    //
    // GET ALL COMMENTS
    //
    @Operation(description = "retrieves a page of comments for a post")
    @GetMapping()
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Error in the request parameters"),
        @ApiResponse(responseCode = "404", description = "The specified post could not be found"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    public Page<Comment> getAllComments(
        @Parameter(description = "id of the post whose comments to fetch")
            @PathVariable("postId") Long commentId,
        @Parameter(description = "page data of the posts to fetch")
            @AllowSortFields(value = {"date", "author"})
            @ParameterObject @PageableDefault(sort = {"date"}, direction = Sort.Direction.DESC) Pageable pageable);

    //
    // DELETE COMMENT
    //
    @Operation(description = "deletes a comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Authentification problem"),
        @ApiResponse(responseCode = "403", description = "Not authorized to perform the action"),
        @ApiResponse(responseCode = "404", description = "The specified comment could not be found"),
        @ApiResponse(responseCode = "200", description = "Successfull request") })
    @DeleteMapping("/{commentId}")
    public Long deleteComment(
        @Parameter(description = "id of the post whose comment to delete")
            @PathVariable("postId") Long postId,
        @Parameter(description = "id of the comment to delete")
            @PathVariable("commentId") Long commentId);

}
