package com.example.news.dto.news;

import com.example.news.validation.UserFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@UserFilterValid
public class NewsListResponse {

    private List<NewsWithoutContactsResponse> newses;

}
