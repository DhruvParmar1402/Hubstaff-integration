package com.hubstaff.integration.service.activity;

import com.hubstaff.integration.dto.*;

import java.time.LocalDate;

import com.hubstaff.integration.entity.ActivityEntity;
import com.hubstaff.integration.exception.ExternalApiException;
import com.hubstaff.integration.repository.ActivityRepository;
import com.hubstaff.integration.service.application.ApplicationServiceImpl;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import com.hubstaff.integration.service.token.TokenServiceImpl;
import com.hubstaff.integration.util.ObjectUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityServiceInterface {
    @Value("${base.api.url}")
    private String baseUrl;

    @Value("${fetch.organization.url}")
    private String fetchOrganizationUrl;

    @Value("${fetch.dailyActivities.url}")
    private String fetchActivityDaily;

    private final ModelMapper mapper;
    private final TokenServiceImpl tokenServiceImpl;
    private final OrganizationServiceImpl organizationServiceImpl;
    private final ApplicationServiceImpl applicationServiceImpl;
    private final RestTemplate restTemplate;
    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ModelMapper mapper, TokenServiceImpl tokenServiceImpl, OrganizationServiceImpl organizationServiceImpl, ApplicationServiceImpl applicationServiceImpl, RestTemplate restTemplate, ActivityRepository activityRepository) {
        this.mapper = mapper;
        this.tokenServiceImpl = tokenServiceImpl;
        this.organizationServiceImpl = organizationServiceImpl;
        this.applicationServiceImpl = applicationServiceImpl;
        this.restTemplate = restTemplate;
        this.activityRepository = activityRepository;
    }

    public void fetchAndSaveActivities() {

        List<OrganizationDTO> organizations = organizationServiceImpl.getOrganizations();

        LocalDate yesterday = LocalDate.now(ZoneOffset.UTC).minusDays(1);

        ZonedDateTime dayStart = yesterday.atStartOfDay(ZoneOffset.UTC);

        ZonedDateTime dayEnd = yesterday.atTime(23, 59, 59).atZone(ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        String startFormatted = dayStart.format(formatter) + "&";
        String endFormatted = dayEnd.format(formatter);

        String token = tokenServiceImpl.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {
            for (OrganizationDTO organization : organizations) {
                String finalUrl = baseUrl + fetchOrganizationUrl + "/"
                        + organization.getOrganizationId().toString() + "/"
                        + fetchActivityDaily + "?date[start]=" + startFormatted + "date[stop]=" + endFormatted;

                ResponseEntity<ActivityResponse> response = restTemplate.exchange(
                        finalUrl,
                        HttpMethod.GET,
                        requestEntity,
                        ActivityResponse.class
                );

                List<ActivityDTO> activities = null;
                if (ObjectUtil.isNullOrEmpty(response.getBody()) || ObjectUtil.isNullOrEmpty(response.getBody().getActivities())) {
                    continue;
                }

                activities = response.getBody().getActivities();

                System.out.println(activities);

                for (ActivityDTO activity : activities) {
                    activity.setOrganizationId(organization.getOrganizationId());
                    activityRepository.save(mapper.map(activity, ActivityEntity.class));
                    applicationServiceImpl.save(mapper.map(activity, ApplicationDTO.class));
                }
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException("Hubstaff API error: " + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Failed to connect to Hubstaff API", 503, e);
        }
    }

    public ApplicationActivityDTO getTotalTimeSpentOnAppByProject(ActivityDTO activityDTO) {
        Long projectId=activityDTO.getProjectId();
        String appName=activityDTO.getAppName();

        List<ActivityEntity> timeSpent= activityRepository.getTotalTimeSpentOnAppByProject(projectId,appName);
        int totalTracked = timeSpent.stream()
                .reduce(0, (sum, activity) -> sum + activity.getTracked(), Integer::sum);

        return new ApplicationActivityDTO("Total time spend by project: "+projectId+" on app "+appName+" is: "+totalTracked,totalTracked);
    }

    public ApplicationActivityDTO getTotalTimeSpentOnAppByOrganization(ActivityDTO activityDTO) {
        Integer organizationId=activityDTO.getOrganizationId();
        String appName=activityDTO.getAppName();
        List<ActivityEntity> timeSpent= activityRepository.getTotalTimeSpentOnAppByOrganization(organizationId,appName);

        int totalTracked = timeSpent.stream()
                .reduce(0, (sum, activity) -> sum + activity.getTracked(), Integer::sum);

        return new ApplicationActivityDTO("Total time spend by organization with id: "+organizationId+" on app "+appName+" is: "+totalTracked,totalTracked);
    }
    public ApplicationActivityDTO getTotalTimeSpentOnApp(ActivityDTO activityDTO) {
        Long userId=activityDTO.getUserId();
        String appName=activityDTO.getAppName();
        List<ActivityEntity> timeSpent= activityRepository.getTotalTimeSpentOnApp(userId,appName);
        int totalTracked = timeSpent.stream()
                .reduce(0, (sum, activity) -> sum + activity.getTracked(), Integer::sum);
        return new ApplicationActivityDTO("Total time spend by user "+userId+" on app "+appName+" is: "+totalTracked,totalTracked);
    }
}


