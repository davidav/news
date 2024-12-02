package com.example.news.controller;

import com.example.news.dto.ErrorResponse;
import com.example.news.dto.PagesRequest;
import com.example.news.dto.mapper.UserMapper;
import com.example.news.dto.user.*;
import com.example.news.model.User;
import com.example.news.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;



    @Operation(
            summary = "Get paginated all users matching the filter",
            description = "Get all users by filter. Returns paginated users matching the filter. " +
                    "Available only to users with a role ADMIN",
            tags = {"user"}
    )
    @GetMapping("/filter")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> findAllByFilter(@Valid UserFilter filter) {
        return ResponseEntity.ok(
                userMapper.userListToUserListResponse(
                        userService.filterBy(filter)));
    }

    @Operation(
            summary = "Get paginated all users",
            description = "Get all users. Return list of paginated users. Available only to users with a role ADMIN",
            tags = {"user"}
    )
    @GetMapping
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> findAll(@Valid PagesRequest request) {

        return ResponseEntity.ok(
                userMapper.userListToUserListResponse(
                        userService.findAll(request)));
    }

    @Operation(
            summary = "Get user by id",
            description = "Return firstName, secondName, countNewses, countComments user's with a specific ID. " +
                    "Available only to users with a roles ADMIN, MODERATOR or USER himself",
            tags = {"user", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.findById(id, userDetails)));
    }

    @Operation(
            summary = "Create new user",
            description = "Return new user. Available only to users with a role ADMIN",
            tags = {"user", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PostMapping//("/create")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")//
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        User newUser = userService.save(userMapper.requestCreateToUser(request, passwordEncoder));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(newUser));
    }

    @Operation(
            summary = "Edit user",
            description = "Return edited user. Available only to users with a roles ADMIN, MODERATOR or USER himself",
            tags = {"user", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody  @Valid UpsertUserRequest request,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User updateUser = userService.update(userMapper.requestUpdateToUser(id, request), userDetails);

        return ResponseEntity.ok(userMapper.userToResponse(updateUser));
    }

    @Operation(
            summary = "Delete user",
            description = "Delete user with a specific ID. " +
                    "Available only to users with a roles ADMIN, MODERATOR or USER himself",
            tags = {"user", "id"}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteById(id, userDetails);

        return ResponseEntity.noContent().build();
    }

}
