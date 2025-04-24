package com.hubstaff.integration.util;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
    private Object data;
    private String lastEvaluatedKey;
    private int limit;
    private boolean hasMore;
}