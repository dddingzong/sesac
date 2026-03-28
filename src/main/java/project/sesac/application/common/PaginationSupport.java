package project.sesac.application.common;

import java.util.List;

public final class PaginationSupport {

    private PaginationSupport() {
    }

    public static <T> PaginationResult<T> paginate(List<T> source, int page, int pageSize, int navigationSize) {
        int safePage = Math.max(page, 1);
        int totalCount = source.size();
        int totalPages = totalCount == 0 ? 0 : (totalCount - 1) / pageSize + 1;
        int fromIndex = Math.min((safePage - 1) * pageSize, totalCount);
        int toIndex = Math.min(fromIndex + pageSize, totalCount);
        int startPage = safePage % navigationSize == 0
                ? safePage - navigationSize + 1
                : safePage - safePage % navigationSize + 1;

        return new PaginationResult<>(
                source.subList(fromIndex, toIndex),
                totalCount,
                totalPages,
                startPage,
                safePage
        );
    }
}
