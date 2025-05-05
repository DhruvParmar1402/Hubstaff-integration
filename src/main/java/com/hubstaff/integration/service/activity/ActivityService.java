package com.hubstaff.integration.service.activity;

import com.hubstaff.integration.dto.ActivityDTO;
import com.hubstaff.integration.dto.ApplicationActivityDTO;
import com.hubstaff.integration.dto.AppsWithTrendResponse;
import com.hubstaff.integration.dto.TrendOfTopFiveApps;
import com.hubstaff.integration.exception.EntityNotFound;

import java.util.List;
import java.util.Map;


public interface ActivityService {
    void fetchAndSaveActivities() throws EntityNotFound;
    ApplicationActivityDTO getTotalTimeSpentOnAppByProject(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnAppByOrganization(ActivityDTO activityDTO);
    ApplicationActivityDTO getTotalTimeSpentOnApp(ActivityDTO activityDTO);
    List<Map.Entry<Long, Integer>> getMostActiveUsers(Integer organizationId);
    AppsWithTrendResponse getAppUsedThisMonthWithTrend(Integer organizationId) throws EntityNotFound;
    List<TrendOfTopFiveApps> getTopFiveApps(Integer organizationId);
}
