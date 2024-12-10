package com.karczewski.its.api.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PageWrapper<T> {

    private Collection<T> data;
    private long totalCount;
    private int totalPages;
    private int pageNumber;
    private long offset;
    private long limit;
    private boolean hasNextPage;
    private NextPage next;

    public static <T> PageWrapper<T> from(final Page<T> page) {
        final Pageable pageable = page.getPageable();
        final NextPage next = NextPage.builder()
                .limit(pageable.next().getPageSize())
                .offset(pageable.next().getOffset())
                .build();
        return PageWrapper.<T>builder()
                .data(page.getContent())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(pageable.getPageNumber())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .hasNextPage(page.hasNext())
                .next(next)
                .build();
    }
}
