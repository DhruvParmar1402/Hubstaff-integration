package com.hubstaff.integration.schedulers;

import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.service.activity.ActivityService;
import com.hubstaff.integration.service.activity.ActivityServiceImpl;
import com.hubstaff.integration.service.organization.OrganizationService;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import com.hubstaff.integration.service.user.UserService;
import com.hubstaff.integration.service.user.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Schedulers {
    private final ActivityService activityService;
    private final OrganizationService organizationService;
    private final UserService userService;

    public Schedulers(ActivityServiceImpl activityService, OrganizationServiceImpl organizationService, UserServiceImpl userService)
    {
        this.activityService=activityService;
        this.organizationService=organizationService;
        this.userService=userService;
    }

//    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedRate = 10000)
    public void fetchAndSaveData() throws EntityNotFound {
//        activityService.fetchAndSaveActivities();
//        organizationService.fetchAndSave();
//        userService.fetchAndSaveUsers();
    }
}


