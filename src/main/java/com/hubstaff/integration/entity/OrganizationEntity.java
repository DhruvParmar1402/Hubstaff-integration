package com.hubstaff.integration.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Organizations")
public class OrganizationEntity {
    @DynamoDBHashKey(attributeName = "organizationName")
    @DynamoDBAttribute(attributeName = "organizationName")
    private String organizationName;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "organizationId_index", attributeName = "organizationId")
    @DynamoDBAttribute(attributeName = "organizationId")
    private Integer organizationId;

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "createdAt_status_index",attributeName = "status")
    @DynamoDBAttribute(attributeName = "status")
    private String status;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "createdAt_status_index",attributeName = "createdAt")
    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;

    @DynamoDBAttribute(attributeName = "updatedAt")
    private String updatedAt;

    @DynamoDBAttribute(attributeName = "metadata")
    private Map<String, Object> metadata;

    @DynamoDBAttribute(attributeName = "inviteUrl")
    private String inviteUrl;
}
