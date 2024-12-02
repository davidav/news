package com.example.news.service;

import com.example.news.dto.PagesRequest;
import com.example.news.model.Comment;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {

    List<Comment> findAll(PagesRequest request);

    Comment findById(Long id);

    Comment save(Comment comment, UserDetails userDetails);

    Comment update(Comment comment, UserDetails userDetails);

    void deleteById(Long id, UserDetails userDetails);

}
