package com.example.news.dto.mapper;

import com.example.news.dto.user.CreateUserRequest;
import com.example.news.dto.user.UpsertUserRequest;
import com.example.news.dto.user.UserListResponse;
import com.example.news.dto.user.UserResponse;
import com.example.news.model.User;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;


@DecoratedWith(UserMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    UserResponse userToResponse(User user);

    User requestCreateToUser(CreateUserRequest request, PasswordEncoder passwordEncoder);

    User requestUpdateToUser(Long id, UpsertUserRequest request);

    default UserListResponse userListToUserListResponse(List<User> users){
        UserListResponse response = new UserListResponse();
        response.setUsers(users.stream().map(this::userToResponse).collect(Collectors.toList()));
        return response;
    }

}
