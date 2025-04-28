package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hubstaff.integration.entity.User;
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

    public void save(User user) {
        repo.save(user);
    }

    public List<User> findUserByOrganization(String organizationName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":organizationName",new AttributeValue().withS(organizationName));

        DynamoDBQueryExpression<User> expression=new DynamoDBQueryExpression<User>()
                .withIndexName("organizationName_index")
                .withKeyConditionExpression("organizationName = :organizationName")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(User.class,expression);
    }
}
