package com.hubstaff.integration.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Users")
public class UserEntity {

    @DynamoDBHashKey(attributeName = "email")
    @DynamoDBAttribute(attributeName = "email")
    private String email;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "organizationId_index",attributeName = "organizationId")
    @DynamoDBAttribute(attributeName = "organizationId")
    private Integer organizationId;

    @DynamoDBAttribute(attributeName = "userId")
    private Integer userId;

    @DynamoDBAttribute(attributeName = "organizationName")
    private String organizationName;

    @DynamoDBAttribute(attributeName = "firstName")
    private String firstName;

    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;

    @DynamoDBAttribute(attributeName = "timeZone")
    private String timeZone;

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "createdAt_status_index",attributeName = "status")
    @DynamoDBAttribute(attributeName = "status")
    private String status;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "createdAt_status_index",attributeName = "createdAt")
    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;

    @DynamoDBAttribute(attributeName = "updatedAt")
    private String updatedAt;
}
