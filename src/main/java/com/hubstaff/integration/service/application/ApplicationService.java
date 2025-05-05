package com.hubstaff.integration.service.application;

import com.hubstaff.integration.dto.ActivityDTO;
import com.hubstaff.integration.dto.ApplicationDTO;
import com.hubstaff.integration.exception.EntityNotFound;

import java.util.List;
import java.util.Map;

public interface ApplicationService {
    void save(ApplicationDTO applicationDTO, ActivityDTO activityDTO);
    List<ApplicationDTO> fetchByUserId(Integer userId) throws EntityNotFound;
    List<String> fetchNewApps(Integer organizationId) throws EntityNotFound;
    List<Map.Entry<String , Integer>> getTopFiveApps(Integer organizationId) throws EntityNotFound;
    List<String> getAppsUsedCurrentMonth(Integer organizationId);
}
