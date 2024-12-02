package com.example.news.service;

import com.example.news.dto.PagesRequest;
import com.example.news.dto.user.UserFilter;
import com.example.news.dto.user.UserForTest;
import com.example.news.model.User;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;

public interface UserService {

    List<User> filterBy(UserFilter filter);

    List<User> findAll(PagesRequest request);

    User findById(Long id, UserDetails userDetails);

    User save(User user);

    User update(User user, UserDetails userDetails);

    void deleteById(Long id, UserDetails userDetails);

    User findByLogin(String login);

    User createUser(UserForTest testUser);
}
