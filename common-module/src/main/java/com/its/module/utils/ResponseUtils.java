package com.its.module.utils;

import com.its.module.model.response.Meta;
import com.its.module.model.response.Response;

import java.util.ArrayList;

public class ResponseUtils {
    public static Response<?> emptyListResponse(int pageSize) {
        Meta meta = Meta.builder()
                .page(1)
                .pageSize(pageSize)
                .totalPages(1)
                .totalElements(0)
                .build();
        return Response.builder()
                    .data(new ArrayList<>())
                    .meta(meta)
                    .build();
    }
}
