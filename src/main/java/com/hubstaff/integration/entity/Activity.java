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
public class Activity {
    @DynamoDBHashKey
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "userId_appName_index")
    private Long userId;

    @DynamoDBRangeKey
    private Long activityId;

    @DynamoDBAttribute
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "organizationId_createdAt_index")
    private Long organizationId;

    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"projectId_appName_index","userId_appName_index"})
    @DynamoDBAttribute
    private String appName;

    @DynamoDBAttribute
    private String date;

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "organizationId_createdAt_index")
    @DynamoDBAttribute
    private String createdAt;

    @DynamoDBAttribute
    private String updatedAt;

    @DynamoDBAttribute
    private Long taskId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "projectId_appName_index")
    @DynamoDBAttribute
    private Long projectId;

    @DynamoDBAttribute
    private Integer tracked;

    @DynamoDBAttribute
    private Integer activations;
}

