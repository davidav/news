package com.example.news.service;

import com.example.news.dto.PagesRequest;
import com.example.news.dto.category.CategoryFilter;
import com.example.news.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll(PagesRequest request);

    Category findById(Long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);

    List<Category> filterBy(CategoryFilter filter);
}
