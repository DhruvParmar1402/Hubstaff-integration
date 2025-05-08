package com.hubstaff.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Top5AppsThisMonthResponse {
    private String appName;
    private Integer totalNumberOfUser;
}
