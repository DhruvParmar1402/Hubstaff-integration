package com.hubstaff.integration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.hubstaff.integration.entity.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganizationRepository {

    private final DynamoDBMapper repo;

    public OrganizationRepository(DynamoDBMapper repo)
    {
        this.repo=repo;
    }

    public void save(Organization organization) {
        repo.save(organization);
    }

    public List<Organization> getOrganizations() {
        return repo.scan(Organization.class,new DynamoDBScanExpression());
    }
}
