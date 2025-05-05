package com.hubstaff.integration.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Users")
public class User {

    @DynamoDBHashKey
    private String email;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "organizationId_createdAt_index")
    private Integer organizationId;

    private Integer userId;

    private String organizationName;

    private String firstName;

    private String lastName;

    private String timeZone;

    private String status;

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "organizationId_createdAt_index")
    private String createdAt;

    private String updatedAt;
}
