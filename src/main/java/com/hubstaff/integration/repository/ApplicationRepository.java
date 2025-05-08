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

    public List<Application> fetchByUserIdAndAppName(Integer userId,String appName) {
        Map<String,AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withN(Integer.toString(userId)));
        String condition="userId = :userId";

        if(appName!=null)
        {
            eav.put(":appName",new AttributeValue().withS(appName));
            condition+=" AND appName = :appName";
        }

        DynamoDBQueryExpression<Application> expression=new DynamoDBQueryExpression<Application>()
                .withKeyConditionExpression(condition)
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(Application.class,expression);
    }

    public List<Application> fetchNewApplications(Integer organizationId, String startDate, String endDate) {
        Map<String , AttributeValue> eav=new HashMap<>();

        eav.put(":organizationId", new AttributeValue().withN(Integer.toString(organizationId)));
        eav.put(":startDate", new AttributeValue().withS(startDate));
        eav.put(":endDate", new AttributeValue().withS(endDate));

        DynamoDBQueryExpression<Application> expression=new DynamoDBQueryExpression<Application>()
                .withIndexName("organizationId_addedAt_index")
                    .withKeyConditionExpression("organizationId = :organizationId AND addedAt BETWEEN :startDate AND :endDate")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(Application.class, expression);
    }

    public List<Application> getAppsUsedBetweenStartAndEnd(Integer organizationId, String startDate, String endDate) {
        Map<String , AttributeValue> eav=new HashMap<>();

        eav.put(":organizationId", new AttributeValue().withN(Integer.toString(organizationId)));
        eav.put(":startDate", new AttributeValue().withS(startDate));
        eav.put(":endDate", new AttributeValue().withS(endDate));

        DynamoDBQueryExpression<Application> expression=new DynamoDBQueryExpression<Application>()
                .withIndexName("organizationId_lastUsedAt_index")
                .withKeyConditionExpression("organizationId = :organizationId AND lastUsedAt BETWEEN :startDate AND :endDate")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(Application.class, expression);
    }
}
