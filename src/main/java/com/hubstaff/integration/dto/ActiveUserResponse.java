package com.hubstaff.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveUserResponse {
    private Long userId;
    private Integer totalTimeSpent;
}
