package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hubstaff.integration.entity.IntegrationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository {

    private final DynamoDBMapper repo;

    public TokenRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(IntegrationEntity entity) {
        repo.save(entity);
    }

    public String getAccessToken(String clientId) {
        return repo.load(IntegrationEntity.class,clientId).getAccessToken();
    }

    public IntegrationEntity getRefreshToken(String clientId) {
        return repo.load(IntegrationEntity.class,clientId);
    }
}
