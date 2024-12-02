package com.example.news.dto.news;

import com.example.news.dto.comment.CommentResponse;
import com.example.news.validation.UserFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@UserFilterValid
public class NewsResponse {


    private String title;

    private String text;

    private List<CommentResponse> comments;

    private Long userId;

    private Long categoryId;

    public void addComment(CommentResponse commentResponse) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(commentResponse);
    }

}
