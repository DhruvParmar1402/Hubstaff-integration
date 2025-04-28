package com.hubstaff.integration.service.activity;

import com.hubstaff.integration.dto.ActivityDTO;
import com.hubstaff.integration.dto.ApplicationActivityDTO;
import com.hubstaff.integration.exception.EntityNotFound;

public interface ActivityService {
    void fetchAndSaveActivities() throws EntityNotFound;
    ApplicationActivityDTO getTotalTimeSpentOnAppByProject(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnAppByOrganization(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnApp(ActivityDTO activityDTO);
}
