package com.hubstaff.integration.service.activity;

import com.hubstaff.integration.dto.*;
import com.hubstaff.integration.entity.Activity;
import com.hubstaff.integration.exception.EntityNotFound;

import java.util.List;
import java.util.Map;


public interface ActivityService {
    void fetchAndSaveActivities() throws EntityNotFound;
    ApplicationActivityDTO getTotalTimeSpentOnAppByProject(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnAppByOrganization(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnApp(ActivityDTO activityDTO);
    List<ActiveUserResponse> getMostActiveUsers(Integer organizationId);
    List<AppsWithTrendResponse> getAppUsedThisMonthWithTrend(Integer organizationId) throws EntityNotFound;
    List<TrendOfTopFiveApps> getTopFiveApps(Integer organizationId);
}
