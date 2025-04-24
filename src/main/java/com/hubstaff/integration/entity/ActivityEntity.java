package com.hubstaff.integration.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@DynamoDBTable(tableName = "Activities")
public class ActivityEntity {
    @DynamoDBHashKey(attributeName = "userId")
    private Long userId;

    @DynamoDBRangeKey(attributeName = "activityId")
    private Long activityId;

    @DynamoDBAttribute(attributeName = "organizationId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "organizationId_appName_index",attributeName = "organizationId")
    private Long organizationId;

    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"projectId_appName_index", "organizationId_appName_index"}, attributeName = "appName")
    @DynamoDBAttribute(attributeName = "appName")
    private String appName;

    @DynamoDBAttribute(attributeName = "date")
    private String date;

    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;

    @DynamoDBAttribute(attributeName = "updatedAt")
    private String updatedAt;

    @DynamoDBAttribute(attributeName = "taskId")
    private Long taskId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "projectId_appName_index",attributeName = "projectId")
    @DynamoDBAttribute(attributeName = "projectId")
    private Long projectId;

    @DynamoDBAttribute(attributeName = "tracked")
    private Integer tracked;

    @DynamoDBAttribute(attributeName = "activations")
    private Integer activations;
}

