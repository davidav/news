package com.example.news.dto.mapper;

import com.example.news.dto.comment.CommentResponse;
import com.example.news.dto.comment.UpsertCommentRequest;
import com.example.news.model.Comment;
import com.example.news.model.News;
import com.example.news.model.User;
import com.example.news.service.NewsService;
import com.example.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class CommentMapperDelegate implements CommentMapper{

    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;


    @Override
    public Comment requestToComment(UpsertCommentRequest request,UserDetails userDetails) {
        Comment comment = new Comment();

        comment.setComment(request.getComment());
        User user = userService.findByLogin(userDetails.getUsername());
        comment.setUser(user);
        News news = newsService.findById(request.getNewsId());
        comment.setNews(news);

        return comment;
    }

    @Override
    public Comment requestToComment(Long id, UpsertCommentRequest request, UserDetails userDetails) {
        Comment comment = requestToComment(request, userDetails);
        comment.setId(id);

        return comment;
    }


    @Override
    public CommentResponse commentToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setComment(comment.getComment());
        response.setUserId(comment.getUser().getId());
        response.setNewsId(comment.getNews().getId());

        return response;
    }
}
