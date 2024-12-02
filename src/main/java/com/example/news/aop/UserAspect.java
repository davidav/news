package com.example.news.aop;

import com.example.news.model.User;
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
public class UserAspect {

    private final UserRepository userRepository;

    @Before("@annotation(com.example.news.aop.UserActionByIdAvailable)")
    public void userActionByIdBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        Long id;
        User user;
        try {
            id = (Long) args[0];
        } catch (ClassCastException ex) {
            user = (User) args[0];
            id = user.getId();
        }
        UserDetails userDetails = (UserDetails) args[1];
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        User requestingUser = userRepository.findByLogin(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(MessageFormatter.format(
                        "UserAspect -> User with login {} not found", userDetails.getUsername()).getMessage()));

        for (String role : roles) {
            if (role.equals("ROLE_ADMIN") || role.equals("ROLE_MODERATOR")) {
                break;
            } else if (role.equals("ROLE_USER") && roles.size() == 1) {
                if (!Objects.equals(id, requestingUser.getId())) {
                    throw new AuthenticationException("Receive, change, delete information about the user by ID" +
                            " available only to users with a roles ADMIN, MODERATOR or USER himself");
                }
            }
        }
    }

}
