package com.hubstaff.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubstaff.integration.validations.Groups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    @JsonProperty("user_id")
    @NotNull(message = "{activity.userId.null}",groups = Groups.FetchActivityByUser.class)
    private Integer userId;

    @JsonProperty("id")
    private Long activityId;

    @NotNull(message = "{activity.organizationId.null}",groups = Groups.FetchActivityByOrganization.class)
    private Integer organizationId;

    @JsonProperty("name")
    @NotBlank(message = "{activity.appName.blank}",groups = {Groups.FetchActivityByUser.class, Groups.FetchActivityByOrganization.class, Groups.FetchActivityByProject.class})
    private String appName;

    @JsonProperty("date")
    private String date;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("task_id")
    private Long taskId;

    @NotNull(message = "{activity.projectId.null}", groups = {Groups.FetchActivityByProject.class})
    @JsonProperty("project_id")
    private Long projectId;

    @JsonProperty("tracked")
    private Integer tracked;

    @JsonProperty("activations")
    private Integer activations;
}
