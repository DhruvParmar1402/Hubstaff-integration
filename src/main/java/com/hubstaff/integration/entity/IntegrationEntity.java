package com.hubstaff.integration.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Integrations")
public class IntegrationEntity {
    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "clientId")
    private String clientId;

    @DynamoDBAttribute(attributeName = "clientSecret")
    private String clientSecret;

    @DynamoDBAttribute(attributeName = "accessToken")
    private String accessToken;

    @DynamoDBAttribute(attributeName = "refreshToken")
    private String refreshToken;

    @DynamoDBAttribute(attributeName = "redirectUri")
    private String redirectUri;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "expiresAt_index", attributeName = "expiresAt")
    @DynamoDBAttribute(attributeName = "expiresAt")
    private String expiresIn;

    @DynamoDBAttribute(attributeName = "createdAt")
    private Date createdAt;

    @DynamoDBAttribute(attributeName = "updatedAt")
    private Date updatedAt;
}
