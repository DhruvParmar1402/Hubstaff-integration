package com.hubstaff.integration.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("email")
    private String email;

    private Integer organizationId;

    @JsonProperty("id")
    private Integer userId;

    private String organizationName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("time_zone")
    private String timeZone;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

}
