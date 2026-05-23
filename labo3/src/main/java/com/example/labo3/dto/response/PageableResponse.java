package com.example.labo3.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {

    private List<T> content;

    private Integer page;
    private Integer size;

    private Long totalElements;
    private Integer totalPages;

    private Boolean last;

    private String sortBy;
    private String sortOrder;
}