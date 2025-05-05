package com.hubstaff.integration.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Applications")
public class Application {
    @DynamoDBHashKey
    private Integer userId;

    @DynamoDBIndexHashKey(globalSecondaryIndexNames = {"organizationId_addedAt_index","organizationId_lastUsedAt_index"})
    @DynamoDBAttribute
    private Integer organizationId;

    @DynamoDBRangeKey
    private String appName;

    @DynamoDBAttribute
    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = "organizationId_addedAt_index")
    private String addedAt;

    @DynamoDBAttribute
    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"organizationId_lastUsedAt_index"})
    private String lastUsedAt;
}
