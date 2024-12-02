package com.example.news.dto.comment;

import com.example.news.validation.UserFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@UserFilterValid
public class CommentListResponse {

    private List<CommentResponse> comments;

}
