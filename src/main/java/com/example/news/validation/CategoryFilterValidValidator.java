package com.example.news.validation;


import com.example.news.dto.category.CategoryFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class CategoryFilterValidValidator implements ConstraintValidator<CategoryFilterValid, CategoryFilter> {
    @Override
    public boolean isValid(CategoryFilter value, ConstraintValidatorContext context) {
        if (ObjectUtils.anyNull(value.getPageNumber(), value.getPageSize())){
            return false;
        }
        return true;
    }
}
