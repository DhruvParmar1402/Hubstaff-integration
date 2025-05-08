package com.hubstaff.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ActivityResponse {
    @JsonProperty("daily_applications")
    private List<ActivityDTO> activities;

    @JsonProperty("pagination")
    private Pagination page;
}
