package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hubstaff.integration.entity.ActivityEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ActivityRepository {

    private final DynamoDBMapper repo;

    public ActivityRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(ActivityEntity activity) {
        repo.save(activity);
    }

    public List<ActivityEntity> getTotalTimeSpentOnApp(Long userId, String appName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withN(Long.toString(userId)));
        eav.put(":appName",new AttributeValue().withS(appName));

        DynamoDBQueryExpression<ActivityEntity> expression=new DynamoDBQueryExpression<ActivityEntity>()
                .withIndexName("userId_appName_index")
                .withKeyConditionExpression("userId = :userId AND appName=:appName")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);
        return repo.query(ActivityEntity.class,expression);
    }

    public List<ActivityEntity> getTotalTimeSpentOnAppByProject(Long projectId, String appName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":projectId",new AttributeValue().withN(Long.toString(projectId)));
        eav.put(":appName",new AttributeValue().withS(appName));

        DynamoDBQueryExpression<ActivityEntity> expression=new DynamoDBQueryExpression<ActivityEntity>()
                .withIndexName("projectId_appName_index")
                .withKeyConditionExpression("projectId = :projectId AND appName=:appName")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);
        return repo.query(ActivityEntity.class,expression);
    }

    public List<ActivityEntity> getTotalTimeSpentOnAppByOrganization(Integer organizationId, String appName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":organizationId",new AttributeValue().withN(Long.toString(organizationId)));
        eav.put(":appName",new AttributeValue().withS(appName));

        DynamoDBQueryExpression<ActivityEntity> expression=new DynamoDBQueryExpression<ActivityEntity>()
                .withIndexName("organizationId_appName_index")
                .withKeyConditionExpression("organizationId = :organizationId AND appName=:appName")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);
        return repo.query(ActivityEntity.class,expression);
    }
}
