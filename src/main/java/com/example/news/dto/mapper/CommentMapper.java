package com.example.news.dto.mapper;

import com.example.news.dto.comment.UpsertCommentRequest;
import com.example.news.dto.comment.CommentListResponse;
import com.example.news.dto.comment.CommentResponse;
import com.example.news.model.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;
@DecoratedWith(CommentMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    default CommentListResponse commentListToCommentListResponse(List<Comment> comments){
        CommentListResponse response = new CommentListResponse();
        response.setComments(comments.stream().map(this::commentToResponse).collect(Collectors.toList()));
        return response;
    }

    CommentResponse commentToResponse(Comment comment);

    Comment requestToComment(UpsertCommentRequest request, UserDetails userDetails);

    Comment requestToComment(Long id, UpsertCommentRequest request, UserDetails userDetails);
}
