package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hubstaff.integration.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

    public List<User> findUserByOrganization(Integer organizationId) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":organizationId",new AttributeValue().withN(organizationId.toString()));

        DynamoDBQueryExpression<User> expression=new DynamoDBQueryExpression<User>()
                .withIndexName("organizationId_createdAt_index")
                .withKeyConditionExpression("organizationId = :organizationId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(User.class,expression);
    }



    public List<User> findUserByOrganizationId(Long organizationId, String startOfMonth, String endOfMonth) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":organizationId",new AttributeValue().withN(organizationId.toString()));
        eav.put(":startDate",new AttributeValue().withS(startOfMonth));
        eav.put(":endDate",new AttributeValue().withS(endOfMonth));

        DynamoDBQueryExpression<User> expression=new DynamoDBQueryExpression<User>()
                .withIndexName("organizationId_createdAt_index")
                .withKeyConditionExpression("organizationId = :organizationId AND createdAt BETWEEN :startDate AND :endDate")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(User.class,expression);
    }
}
