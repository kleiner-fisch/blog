package com.example.demo.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * A collection of default values
 */
public class DefaultValues {
    public final static String DEFAULT_SORTING_DIRECTION = "asc";
    public final static String DEFAULT_USER_SORTING_COLUMN = "username";
    public final static String DEFAULT_POST_SORTING_COLUMN = "date";


    public static final int DEFAULT_PAGE_LIMIT = 10;
    public static final int DEFAULT_PAGE_OFFSET = 0;
    public static final String DEFAULT_PAGE_LIMIT_STRING = DEFAULT_PAGE_LIMIT + "";
    public static final String DEFAULT_PAGE_OFFSET_STRING = DEFAULT_PAGE_OFFSET + "";


    public static String DELETED_USER = "deleted user";

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String ROLE_SEPERATOR = ",";

    // public static final Sort DEFAULT_SORTING = Sort.
            // Sort.by(Sort.Direction.fromString(DEFAULT_SORTING_DIRECTION));

    // public static final Pageable DEFAULT_PAGE_REQUEST = 
    //         PageRequest.of(DEFAULT_PAGE_OFFSET, DEFAULT_PAGE_LIMIT, DEFAULT_SORTING);

    public static Pageable getDefaultUserPageRequest(){
        Sort defaultSorting = Sort.by(Sort.Direction.fromString(DEFAULT_SORTING_DIRECTION), DEFAULT_USER_SORTING_COLUMN);
        return PageRequest.of(DEFAULT_PAGE_OFFSET, DEFAULT_PAGE_LIMIT, defaultSorting);
    }
    public static Pageable getDefaultPostPageRequest(){
        Sort defaultSorting = Sort.by(Sort.Direction.fromString(DEFAULT_SORTING_DIRECTION), DEFAULT_POST_SORTING_COLUMN);
        return PageRequest.of(DEFAULT_PAGE_OFFSET, DEFAULT_PAGE_LIMIT, defaultSorting);
    }
}
