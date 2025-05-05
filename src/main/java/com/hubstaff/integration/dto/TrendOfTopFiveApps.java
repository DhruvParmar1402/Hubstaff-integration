package com.hubstaff.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendOfTopFiveApps
{
    private String appName;
    private Integer total;
    private String trend;
}