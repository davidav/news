package com.example.news.repository;

import com.example.news.dto.category.CategoryFilter;
import com.example.news.model.Category;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public interface CategorySpecification {

    static Specification<Category> withFilter(CategoryFilter filter){
        return Specification.where(byName(filter.getName()))
                .and(byId(filter.getId()))
                .and(byCreatedAtBefore(filter.getCreateBefore()))
                .and(byUpdateAtBefore(filter.getUpdateBefore()));
    }

    static Specification<Category> byUpdateAtBefore(Instant updateBefore) {
        return ((root, query, criteriaBuilder) -> {
            if (updateBefore == null){
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updateAt"), updateBefore);
        });
    }

    static Specification<Category> byCreatedAtBefore(Instant createBefore) {
        return ((root, query, criteriaBuilder) -> {
            if (createBefore == null){
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createBefore);
        });

    }
    static Specification<Category> byId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("id"), id);
        };
    }

    static Specification<Category> byName(String name) {
        return (root, query, cb) -> {
            if (name == null){
                return null;
            }
            return cb.equal(root.get("name"), name);
        };
    }


}
