package com.example.news.controller;

import com.example.news.dto.ErrorResponse;
import com.example.news.dto.PagesRequest;
import com.example.news.dto.mapper.NewsMapper;
import com.example.news.dto.news.NewsListResponse;
import com.example.news.dto.news.NewsResponse;
import com.example.news.dto.news.UpsertNewsRequest;
import com.example.news.model.News;
import com.example.news.service.NewsService;
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
@RequestMapping("/api/news")
@Tag(name = "News", description = "News API")
public class NewsController {
    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @Operation(
            summary = "Get paginated all news",
            description = "Get all news. Return list of paginated news." +
                    "Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"news"}
    )
    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<NewsListResponse> findAll(@Valid PagesRequest request) {

        return ResponseEntity.ok(
                newsMapper.newsListToNewsListResponse(
                        newsService.findAll(request)));
    }

    @Operation(
            summary = "Get news by id",
            description = "Return title, text, list of comments, userId, categoryId news's with a specific ID." +
                    "Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"news", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                newsMapper.newsToResponse(
                        newsService.findById(id)));
    }

    @Operation(
            summary = "Create new news",
            description = "Return new news. Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"news", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = NewsResponse.class),
                            mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")}
            )
    })
    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid UpsertNewsRequest request,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        News newNews = newsService.save(newsMapper.requestToNews(request, userDetails), userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsMapper.newsToResponse(newNews));
    }

    @Operation(
            summary = "Edit news",
            description = "Return edited news. Only the creator news can edit it",
            tags = {"news", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(@PathVariable Long id, @RequestBody @Valid UpsertNewsRequest request,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        News updateNews = newsService.update(newsMapper.requestToNews(id, request, userDetails));
        return ResponseEntity.ok(newsMapper.newsToResponse(updateNews));
    }


    @Operation(
            summary = "Delete news",
            description = "Delete news with a specific ID. " +
                    "Available only to users with a roles ADMIN, MODERATOR or USER the creator news",
            tags = {"news", "id"}
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
        newsService.deleteById(id, userDetails);
        return ResponseEntity.noContent().build();
    }

}
