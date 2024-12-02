package com.example.news.service;

import com.example.news.aop.UserActionByIdAvailable;
import com.example.news.dto.PagesRequest;
import com.example.news.dto.user.UserFilter;
import com.example.news.dto.user.UserForTest;
import com.example.news.model.Role;
import com.example.news.model.User;
import com.example.news.repository.UserRepository;
import com.example.news.repository.UserSpecification;
import com.example.news.util.AppHelperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> filterBy(UserFilter filter) {
        return userRepository.findAll(
                (UserSpecification.withFilter(filter)),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())).getContent();
    }

    @Override
    public List<User> findAll(PagesRequest request) {
        return userRepository.findAll(
                PageRequest.of(request.getPageNumber(), request.getPageSize())).getContent();
        }

    @Override
    @UserActionByIdAvailable
    public User findById(Long id, UserDetails userDetails) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormatter.format("User with login {} not found", id).getMessage()));
    }

    @Override
    public User save(User user) {
        List<Role> roles = user.getRoles();
        roles.forEach(role -> role.setUser(user));
        return userRepository.save(user);
    }

    @Override
    @UserActionByIdAvailable
    public User update(User user, UserDetails userDetails) {
        User existedUser = findById(user.getId(), userDetails);
        AppHelperUtils.copyNonNullProperties(user, existedUser);

        return userRepository.save(existedUser);
    }

    @Override
    @UserActionByIdAvailable
    public void deleteById(Long id, UserDetails userDetails) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormatter.format("User with login {} not found", login).getMessage()));
    }

    @Override
    public User createUser(UserForTest testUser) {
        return User.builder()
                .login(testUser.getLogin())
                .password(passwordEncoder.encode(testUser.getPassword()))
                .roles(Collections.singletonList(Role.from(testUser.getRole())))
                .build();
    }
}
