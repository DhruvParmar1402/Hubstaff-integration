package com.hubstaff.integration.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationActivityDTO {
    private String message;
    private Integer totalTimeSpent;
}
