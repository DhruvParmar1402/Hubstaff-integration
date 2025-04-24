package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hubstaff.integration.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {


    private final DynamoDBMapper repo;
    public UserRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(UserEntity user) {
        repo.save(user);
    }

    public List<UserEntity> findUserByOrganization(String organizationName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":organizationName",new AttributeValue().withS(organizationName));

        DynamoDBQueryExpression<UserEntity> expression=new DynamoDBQueryExpression<UserEntity>()
                .withIndexName("organizationName_index")
                .withKeyConditionExpression("organizationName = :organizationName")
                .withExpressionAttributeValues(eav)
                .withScanIndexForward(false)
                .withConsistentRead(false);

        List<UserEntity> users=repo.query(UserEntity.class,expression);
        return users;
    }
}
