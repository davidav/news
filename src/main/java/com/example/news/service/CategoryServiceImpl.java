package com.example.news.service;

import com.example.news.dto.PagesRequest;
import com.example.news.dto.category.CategoryFilter;
import com.example.news.model.Category;
import com.example.news.repository.CategoryRepository;
import com.example.news.repository.CategorySpecification;
import com.example.news.util.AppHelperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> filterBy(CategoryFilter filter) {
        return categoryRepository.findAll(
                (CategorySpecification.withFilter(filter)),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())).getContent();
    }
    @Override
    public List<Category> findAll(PagesRequest request) {
        return categoryRepository.findAll(
                PageRequest.of(request.getPageNumber(), request.getPageSize())).getContent();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormatter.format("Категория с id {} не найдена", id).getMessage()));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Category existedCategory = findById(category.getId());
        AppHelperUtils.copyNonNullProperties(category, existedCategory);

        return categoryRepository.save(existedCategory);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }


}
