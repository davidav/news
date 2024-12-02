package com.example.news.dto.mapper;

import com.example.news.dto.category.CategoryListResponse;
import com.example.news.dto.category.CategoryResponse;
import com.example.news.dto.category.UpsertCategoryRequest;
import com.example.news.model.Category;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(CategoryMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {


    default CategoryListResponse categoryListToCategoryListResponse(List<Category> categories){
        CategoryListResponse response = new CategoryListResponse();
        response.setCategories(
                categories.stream()
                        .map(this::categoryToResponse)
                        .collect(Collectors.toList()));
        return response;
    }

    CategoryResponse categoryToResponse(Category category);

    Category requestToCategory(UpsertCategoryRequest request);

    Category requestToCategory(Long id,UpsertCategoryRequest request);

}
