package com.example.news.controller;

import com.example.news.dto.ErrorResponse;
import com.example.news.dto.PagesRequest;
import com.example.news.dto.comment.CommentListResponse;
import com.example.news.dto.comment.CommentResponse;
import com.example.news.dto.comment.UpsertCommentRequest;
import com.example.news.dto.mapper.CommentMapper;
import com.example.news.model.Comment;
import com.example.news.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "Comment", description = "Comment API")
public class CommentController {
    private final CommentService commentService;

    private final CommentMapper commentMapper;


    @Operation(
            summary = "Get paginated all comments",
            description = "Get all comments. Return list of paginated comments. " +
                    "Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"news"}
    )
    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CommentListResponse> findAll(@Valid PagesRequest request) {

        return ResponseEntity.ok(
                commentMapper.commentListToCommentListResponse(
                        commentService.findAll(request)));
    }

    @Operation(
            summary = "Get comment by id",
            description = "Return comment, userId, newsId comment's with a specific ID. " +
                    "Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                commentMapper.commentToResponse(
                        commentService.findById(id)));
    }

    @Operation(
            summary = "Create new comment",
            description = "Return new comment. Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid UpsertCommentRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        Comment newComment = commentService.save(commentMapper.requestToComment(request, userDetails), userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentMapper.commentToResponse(newComment));
    }

    @Operation(
            summary = "Edit comment",
            description = "Return edited comment. Only the creator comment can edit it",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestBody @Valid UpsertCommentRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentMapper.requestToComment(id, request, userDetails);
        Comment updateComment = commentService.update(comment, userDetails);
        return ResponseEntity.ok(commentMapper.commentToResponse(updateComment));
    }

    @Operation(
            summary = "Delete comment",
            description = "Delete comment with a specific ID. " +
                    "Available only to users with a roles ADMIN, MODERATOR or USER the creator news",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteById(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}
