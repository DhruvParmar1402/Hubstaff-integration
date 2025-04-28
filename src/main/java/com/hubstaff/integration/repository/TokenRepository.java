package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hubstaff.integration.entity.Integration;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository {

    private final DynamoDBMapper repo;

    public TokenRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(Integration entity) {
        repo.save(entity);
    }

    public String getAccessToken(String clientId) {
        Integration token=repo.load(Integration.class,clientId);
        return token==null?null:token.getAccessToken();
    }

    public Integration getRefreshToken(String clientId) {
        return repo.load(Integration.class,clientId);
    }
}
