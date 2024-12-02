package com.example.news.dto.category;

import com.example.news.dto.news.NewsWithoutContactsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private String name;

    private List<NewsWithoutContactsResponse> newses;

    public void addNews(NewsWithoutContactsResponse news) {
        if (newses == null){
            newses = new ArrayList<>();
        }
        newses.add(news);
    }
}
