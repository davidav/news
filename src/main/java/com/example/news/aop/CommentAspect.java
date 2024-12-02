package com.example.news.aop;

import com.example.news.model.Comment;

import com.example.news.model.User;
import com.example.news.repository.CommentRepository;
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
public class CommentAspect {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Before("@annotation(com.example.news.aop.CommentEditAvailable)")
    public void editBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        Comment comment = (Comment) args[0];
        Comment existComment = commentRepository.findById(comment.getId()).orElseThrow(
                () -> new EntityNotFoundException(MessageFormatter.format(
                        "Comment with id {} not found", comment.getId()).getMessage()));
        if (!Objects.equals(comment.getUser().getId(), existComment.getUser().getId())){
            throw new AuthenticationException("Only the creator comment can edit it");
        }

    }

    @Before("@annotation(com.example.news.aop.CommentDeleteAvailable)")
    public void deleteBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        UserDetails userDetails = (UserDetails) args[1];

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        User requestingUser = userRepository.findByLogin(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(MessageFormatter.format(
                        "UserAspect -> User with login {} not found", userDetails.getUsername()).getMessage()));

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(MessageFormatter.format(
                        "Comment with id {} not found", id).getMessage()));
        for (String role : roles) {
            if (role.equals("ROLE_USER") && roles.size() == 1) {
                if (!Objects.equals(requestingUser.getId(), comment.getUser().getId())) {
                    throw new AuthenticationException("Delete comment available only to users with a roles " +
                            "ADMIN, MODERATOR or USER only author");
                }
            }
        }
    }
}
