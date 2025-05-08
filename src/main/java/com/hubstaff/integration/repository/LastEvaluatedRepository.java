package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hubstaff.integration.dto.LastEvaluated;
import org.springframework.stereotype.Repository;

@Repository
public class LastEvaluatedRepository {
    private final DynamoDBMapper repo;

    public LastEvaluatedRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(LastEvaluated lastEvaluated)
    {
        repo.save(lastEvaluated);
    }

    public LastEvaluated fetch(String serviceName,Integer organizationId)
    {
        return repo.load(LastEvaluated.class,serviceName,organizationId);
    }
}
