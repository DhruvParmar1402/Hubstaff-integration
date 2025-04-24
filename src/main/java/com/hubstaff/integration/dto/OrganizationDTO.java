package com.hubstaff.integration.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {

    @JsonProperty("name")
    private String organizationName;

    @JsonProperty("id")
    private Integer organizationId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("invite_url")
    private String inviteUrl;
}
