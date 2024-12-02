package com.example.news.dto.comment;

import com.example.news.validation.UserFilterValid;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@UserFilterValid
public class CommentResponse {

    private String comment;

    private Long userId;

    private Long newsId;

}
