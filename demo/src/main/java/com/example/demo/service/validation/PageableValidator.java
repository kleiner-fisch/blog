package com.example.demo.service.validation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates a list of sort fields within a Pageable against an allowed list.
 */
public class PageableValidator implements ConstraintValidator<AllowSortFields, Pageable> {

    private List<String> allowedSortFields;

    static final String PROPERTY_NOT_FOUND_MESSAGE = "The following sort fields [%s] are not within the allowed fields. "
            + "Allowed sort fields are: [%s]";

    @Override
    public void initialize(AllowSortFields constraintAnnotation) {
        allowedSortFields = Arrays.asList(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(Pageable value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (CollectionUtils.isEmpty(allowedSortFields)) {
            return true;
        }

        // ignore unsorted
        Sort sort = value.getSort();
        if (sort.isUnsorted()) {
            return true;
        }

        String fieldsNotFound = fieldsNotFoundAsCommaDelimited(sort);

        // all found fields are allowed
        if (StringUtils.isEmpty(fieldsNotFound)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.format(PROPERTY_NOT_FOUND_MESSAGE, fieldsNotFound, String.join(",", allowedSortFields)))
                .addConstraintViolation();
        return false;

    }

    private String fieldsNotFoundAsCommaDelimited(Sort sort) {
        String fieldsNotFound = sort.stream()
                .map(order -> order.getProperty())
                .filter(property -> !allowedSortFields.contains(property))
                .collect(Collectors.joining(","));
        return fieldsNotFound;
    }
}