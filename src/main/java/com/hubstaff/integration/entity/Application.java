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
    @DynamoDBHashKey(attributeName = "userId")
    private Integer userId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "organizationId_appName_index",attributeName = "organizationId")
    @DynamoDBAttribute(attributeName = "organizationId")
    private Integer organizationId;

    @DynamoDBRangeKey(attributeName = "appName")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "organizationId_appName_index",attributeName = "appName")
    private String appName;
}
