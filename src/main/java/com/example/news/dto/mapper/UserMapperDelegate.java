package com.example.news.dto.mapper;


import com.example.news.dto.user.CreateUserRequest;
import com.example.news.dto.user.UpsertUserRequest;
import com.example.news.dto.user.UserResponse;
import com.example.news.model.Role;
import com.example.news.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;


public abstract class UserMapperDelegate implements UserMapper {


    @Override
    public UserResponse userToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName(user.getFirstName());
        userResponse.setSecondName(user.getSecondName());
        userResponse.setCountNewses(user.getNewses().size());
        userResponse.setCountComments(user.getComments().size());

        return userResponse;
    }

    @Override
    public User requestCreateToUser(CreateUserRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList(Role.from(request.getRoleType())))
                .build();
    }


    @Override
    public User requestUpdateToUser(Long id, UpsertUserRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .build();
        user.setId(id);
        return user;
    }

}