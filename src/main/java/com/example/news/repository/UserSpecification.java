package com.example.news.repository;

import com.example.news.dto.user.UserFilter;
import com.example.news.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public interface UserSpecification {

    static Specification<User> withFilter(UserFilter filter){
        return Specification.where(byFirstName(filter.getFirstName()))
                .and(bySecondName(filter.getSecondName()))
                .and(byUserId(filter.getUserId()))
                .and(byCreatedAtBefore(filter.getCreateBefore()))
                .and(byUpdateAtBefore(filter.getUpdateBefore()));
    }

    static Specification<User> byUpdateAtBefore(Instant updateBefore) {
        return ((root, query, criteriaBuilder) -> {
            if (updateBefore == null){
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updateAt"), updateBefore);
        });
    }

    static Specification<User> byCreatedAtBefore(Instant createBefore) {
        return ((root, query, criteriaBuilder) -> {
            if (createBefore == null){
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createBefore);
        });

    }

    static Specification<User> byUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    static Specification<User> bySecondName(String secondName) {
        return (root, query, cb) -> {
            if (secondName == null){
                return null;
            }
            return cb.equal(root.get("secondName"), secondName);
        };
    }

    static Specification<User> byFirstName(String firstName) {
        return (root, query, cb) -> {
            if (firstName == null){
                return null;
            }
            return cb.equal(root.get("firstName"), firstName);
        };
    }

}
