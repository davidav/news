package com.example.news.aop;

import com.example.news.model.News;
import com.example.news.model.User;
import com.example.news.repository.NewsRepository;
import com.example.news.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class NewsAspect {
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    @Before("@annotation(com.example.news.aop.NewsEditAvailable)")
    public void editBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        News news = (News) args[0];
        News existNews = newsRepository.findById(news.getId()).orElseThrow(
                () -> new EntityNotFoundException(MessageFormatter.format(
                        "NewsAspect -> News with id {} not found", news.getId()).getMessage()));
        if (!Objects.equals(news.getUser().getId(), existNews.getUser().getId())) {
            throw new AuthenticationException("Editing news can be only author");
        }

    }

    @Before("@annotation(com.example.news.aop.NewsDeleteAvailable)")
    public void deleteBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        UserDetails userDetails = (UserDetails) args[1];
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        User requestingUser = userRepository.findByLogin(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(MessageFormatter.format(
                        "UserAspect -> User with login {} not found", userDetails.getUsername()).getMessage()));
        News news = newsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(MessageFormatter.format(
                        "NewsAspect -> News with id {} not found", id).getMessage()));

        for (String role : roles) {
            if (role.equals("ROLE_USER") && roles.size() == 1) {
                if (!Objects.equals(news.getUser().getId(), requestingUser.getId())) {
                    throw new AuthenticationException("Delete news available only to users with a roles " +
                            "ADMIN, MODERATOR or USER only author");
                }
            }
        }
    }
}
