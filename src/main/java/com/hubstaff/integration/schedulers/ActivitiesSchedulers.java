package com.hubstaff.integration.schedulers;

import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.service.activity.ActivityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActivitiesSchedulers {

    private Logger logger= LoggerFactory.getLogger(ActivitiesSchedulers.class);
    private final ActivityServiceImpl activityServiceImpl;

    public ActivitiesSchedulers(ActivityServiceImpl activityServiceImpl)
    {
        this.activityServiceImpl = activityServiceImpl;
    }

//    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedRate = 5000)
    public void fetchAndSaveActivities() throws EntityNotFound {
        logger.info("Application activity schedulers executed.");
        activityServiceImpl.fetchAndSaveActivities();
    }
}


