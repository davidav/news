package com.example.news.service;

import com.example.news.aop.CommentDeleteAvailable;
import com.example.news.aop.CommentEditAvailable;
import com.example.news.dto.PagesRequest;
import com.example.news.model.Comment;
import com.example.news.repository.CommentRepository;
import com.example.news.util.AppHelperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final NewsService newsService;


    @Override
    public List<Comment> findAll(PagesRequest request) {
        return commentRepository.findAll(
                PageRequest.of(request.getPageNumber(), request.getPageSize())).getContent();
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormatter.format("Comment with id {} not found", id).getMessage()));
    }

    @Override
    @Transactional
    public Comment save(Comment comment, UserDetails userDetails) {
        Comment savedCommit = commentRepository.save(comment);
        userService.update(savedCommit.getUser(), userDetails);
        newsService.update(savedCommit.getNews());
        return savedCommit;
    }

    @Override
    @CommentEditAvailable
    public Comment update(Comment comment, UserDetails userDetails) {
        Comment existedComment = findById(comment.getId());
        AppHelperUtils.copyNonNullProperties(comment, existedComment);

        return commentRepository.save(existedComment);
    }

    @Override
    @CommentDeleteAvailable
    public void deleteById(Long id, UserDetails userDetails) {
        commentRepository.deleteById(id);

    }
}
