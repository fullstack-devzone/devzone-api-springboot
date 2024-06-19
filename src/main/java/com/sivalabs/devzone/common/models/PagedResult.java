package com.sivalabs.devzone.common.models;

import java.util.List;

public record PagedResult<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {

    public PagedResult(List<T> data, long totalElements, int pageNumber, int totalPages) {
        this(
                data,
                totalElements,
                pageNumber,
                totalPages,
                pageNumber == 1,
                pageNumber == totalPages,
                pageNumber < totalPages,
                pageNumber > 1);
    }
}
