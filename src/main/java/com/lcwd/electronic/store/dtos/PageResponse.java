package com.lcwd.electronic.store.dtos;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PageResponse<T>{
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean islastPage;



}
