package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hubstaff.integration.entity.Activity;
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

    public void save(Activity activity) {
        repo.save(activity);
    }

    public List<Activity> getTotalTimeSpentOnApp(Integer userId, String appName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withN(Integer.toString(userId)));
        eav.put(":appName",new AttributeValue().withS(appName));

        DynamoDBQueryExpression<Activity> expression=new DynamoDBQueryExpression<Activity>()
                .withIndexName("userId_appName_index")
                .withKeyConditionExpression("userId = :userId AND appName=:appName")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);
        return repo.query(Activity.class,expression);
    }

    public List<Activity> getTotalTimeSpentOnAppByProject(Long projectId, String appName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":projectId",new AttributeValue().withN(Long.toString(projectId)));
        eav.put(":appName",new AttributeValue().withS(appName));

        DynamoDBQueryExpression<Activity> expression=new DynamoDBQueryExpression<Activity>()
                .withIndexName("projectId_appName_index")
                .withKeyConditionExpression("projectId = :projectId AND appName=:appName")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);
        return repo.query(Activity.class,expression);
    }

    public List<Activity> getTotalTimeSpentOnAppByOrganization(Integer organizationId, String appName) {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":organizationId",new AttributeValue().withN(Long.toString(organizationId)));
        eav.put(":appName",new AttributeValue().withS(appName));

        DynamoDBQueryExpression<Activity> expression=new DynamoDBQueryExpression<Activity>()
                .withIndexName("organizationId_appName_index")
                .withKeyConditionExpression("organizationId = :organizationId AND appName=:appName")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);
        return repo.query(Activity.class,expression);
    }

    public List<Activity> getMonthActivity(Integer organizationId, String startOfMonth, String endOfMonth)
    {
        Map<String , AttributeValue> eav=new HashMap<>();
        eav.put(":organizationId",new AttributeValue().withN(Long.toString(organizationId)));
        eav.put(":startDate",new AttributeValue().withS(startOfMonth));
        eav.put(":endDate", new AttributeValue().withS(endOfMonth));

        DynamoDBQueryExpression<Activity> expression=new DynamoDBQueryExpression<Activity>()
                .withIndexName("organizationId_createdAt_index")
                .withKeyConditionExpression("organizationId = :organizationId AND createdAt BETWEEN :startDate AND :endDate")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return repo.query(Activity.class,expression);
    }
}
