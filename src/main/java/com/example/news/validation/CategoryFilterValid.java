package com.example.news.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryFilterValid {

    String message() default "Поля пагинации должны быть указаны";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
