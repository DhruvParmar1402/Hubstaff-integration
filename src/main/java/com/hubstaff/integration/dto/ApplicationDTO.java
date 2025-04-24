package com.hubstaff.integration.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationDTO {
    private Integer userId;

    private Integer organizationId;

    private String appName;
}
