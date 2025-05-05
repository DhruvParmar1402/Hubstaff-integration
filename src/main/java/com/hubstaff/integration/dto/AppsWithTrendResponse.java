package com.hubstaff.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppsWithTrendResponse {
    private HashSet<String> apps;
    private String trend;
}
