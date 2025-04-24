package com.hubstaff.integration.service.activity;

import com.hubstaff.integration.dto.ActivityDTO;
import com.hubstaff.integration.dto.ApplicationActivityDTO;

public interface ActivityServiceInterface {
    void fetchAndSaveActivities();
    ApplicationActivityDTO getTotalTimeSpentOnAppByProject(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnAppByOrganization(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnApp(ActivityDTO activityDTO);
}
