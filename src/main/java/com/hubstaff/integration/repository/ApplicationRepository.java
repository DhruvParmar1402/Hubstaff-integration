package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hubstaff.integration.entity.Application;
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

    public void save(Application application) {
        repo.save(application);
    }

    public List<Application> fetchByUserId(Integer userId) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withN(Integer.toString(userId)));

        DynamoDBQueryExpression<Application> expression=new DynamoDBQueryExpression<Application>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(Application.class,expression);
    }
}
