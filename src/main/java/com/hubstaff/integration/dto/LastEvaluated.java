package com.hubstaff.integration.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

@DynamoDBTable(tableName = "LastEvaluated")
public class LastEvaluated {
    @DynamoDBHashKey
    private String schedulerName;

    @DynamoDBRangeKey
    private Integer organizationId;

    private Long lastKey;

}
