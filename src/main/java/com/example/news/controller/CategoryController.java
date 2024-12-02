package com.example.news.controller;

import com.example.news.dto.ErrorResponse;
import com.example.news.dto.PagesRequest;
import com.example.news.dto.category.CategoryFilter;
import com.example.news.dto.category.CategoryListResponse;
import com.example.news.dto.category.CategoryResponse;
import com.example.news.dto.category.UpsertCategoryRequest;
import com.example.news.dto.mapper.CategoryMapper;
import com.example.news.model.Category;
import com.example.news.service.CategoryService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@Tag(name = "Category ", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(
            summary = "Get paginated all categories matching the filter",
            description = "Get all categories by filter. Returns paginated categories matching the filter. " +
                    "Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"categories"}
    )
    @GetMapping("/filter")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CategoryListResponse> findAllByFilter(@Valid CategoryFilter filter) {
        return ResponseEntity.ok(
                categoryMapper.categoryListToCategoryListResponse(
                        categoryService.filterBy(filter)));
    }


    @Operation(
            summary = "Get paginated all categories",
            description = "Get all categories. Return list of paginated categories. " +
                    "Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"categories"}
    )

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CategoryListResponse> findAll(@Valid PagesRequest request) {

        return ResponseEntity.ok(
                categoryMapper.categoryListToCategoryListResponse(
                        categoryService.findAll(request)));
    }


    @Operation(
            summary = "Get category by id",
            description = "Return name, list of newses category's with a specific ID. " +
                    "Available only to users with a roles ADMIN, MODERATOR, USER",
            tags = {"category", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(
                categoryMapper.categoryToResponse(
                        categoryService.findById(id)));
    }


    @Operation(
            summary = "Create new category",
            description = "Return new category. Available only to users with a roles ADMIN, MODERATOR",
            tags = {"category", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid UpsertCategoryRequest request) {
        Category newCategory = categoryService.save(categoryMapper.requestToCategory(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryMapper.categoryToResponse(newCategory));
    }

    @Operation(
            summary = "Edit category",
            description = "Return edited category. Available only to users with a roles ADMIN, MODERATOR",
            tags = {"category", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody @Valid UpsertCategoryRequest request) {
        Category updateCategory = categoryService.update(categoryMapper.requestToCategory(id, request));

        return ResponseEntity.ok(categoryMapper.categoryToResponse(updateCategory));
    }


    @Operation(
            summary = "Delete category",
            description = "Delete category with a specific ID. Available only to users with a roles ADMIN, MODERATOR",
            tags = {"category", "id"}
    )
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
