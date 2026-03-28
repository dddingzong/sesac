package project.sesac.application.common;

import java.util.List;

public record PaginationResult<T>(
        List<T> items,
        int totalCount,
        int totalPages,
        int startPage,
        int currentPage
) {
}
