package com.hubstaff.integration.schedulers;

import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrganizationScheduler {

    private Logger logger= LoggerFactory.getLogger(OrganizationScheduler.class);
    private final OrganizationServiceImpl organizationServiceImpl;

    public OrganizationScheduler(OrganizationServiceImpl organizationServiceImpl)
    {
        this.organizationServiceImpl = organizationServiceImpl;
    }

//    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedRate = 5000)
    public void fetchAndSaveOrganizations() throws EntityNotFound {
        logger.info("Organization scheduler executed.");
        organizationServiceImpl.fetchAndSave();
    }
}
