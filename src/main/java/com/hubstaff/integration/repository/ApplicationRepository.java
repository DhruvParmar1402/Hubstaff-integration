package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hubstaff.integration.entity.ApplicationEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ApplicationRepository {
    private final DynamoDBMapper repo;

    public ApplicationRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(ApplicationEntity application) {
        repo.save(application);
    }

    public List<ApplicationEntity> fetchByUserId(Integer userId) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withN(Integer.toString(userId)));

        DynamoDBQueryExpression<ApplicationEntity> expression=new DynamoDBQueryExpression<ApplicationEntity>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        return repo.query(ApplicationEntity.class,expression);
    }
}
