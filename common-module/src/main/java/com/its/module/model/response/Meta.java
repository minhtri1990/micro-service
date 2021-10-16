package com.its.module.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meta {
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
