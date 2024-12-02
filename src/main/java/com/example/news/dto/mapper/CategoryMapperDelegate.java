package com.example.news.dto.mapper;

import com.example.news.dto.category.CategoryResponse;
import com.example.news.model.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;


public abstract class CategoryMapperDelegate implements CategoryMapper {
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public CategoryResponse categoryToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setName(category.getName());
        response.setNewses(category.getNewses().stream()
                .map(newsMapper::newsWithoutContactsToResponse)
                .collect(Collectors.toList()));

        return response;
    }
}
