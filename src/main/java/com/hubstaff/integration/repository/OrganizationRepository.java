package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.hubstaff.integration.entity.OrganizationEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganizationRepository {

    private final DynamoDBMapper repo;

    public OrganizationRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(OrganizationEntity organization) {
        repo.save(organization);
    }

    public List<OrganizationEntity> getOrganizations() {
        return repo.scan(OrganizationEntity.class,new DynamoDBScanExpression());
    }
}
