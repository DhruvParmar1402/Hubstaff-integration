package com.hubstaff.integration.service.activity;

import com.hubstaff.integration.dto.*;

import com.hubstaff.integration.entity.Activity;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.exception.ExternalApiException;
import com.hubstaff.integration.repository.ActivityRepository;
import com.hubstaff.integration.repository.LastEvaluatedRepository;
import com.hubstaff.integration.service.application.ApplicationService;
import com.hubstaff.integration.service.application.ApplicationServiceImpl;
import com.hubstaff.integration.service.organization.OrganizationService;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import com.hubstaff.integration.service.token.TokenService;
import com.hubstaff.integration.service.token.TokenServiceImpl;
import com.hubstaff.integration.util.DateUtil;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    @Value("${base.api.url}")
    private String baseUrl;

    @Value("${fetch.organization.url}")
    private String fetchOrganizationUrl;

    @Value("${fetch.dailyActivities.url}")
    private String fetchActivityDaily;

    private final ModelMapper mapper;
    private final TokenService tokenService;
    private final OrganizationService organizationService;
    private final ApplicationService applicationService;
    private final RestTemplate restTemplate;
    private final ActivityRepository activityRepository;
    private final MessageSourceImpl messageSource;
    private final LastEvaluatedRepository lastEvaluatedRepository;
    private final String schedulerName="activity";

    public ActivityServiceImpl(ModelMapper mapper, TokenServiceImpl tokenService, OrganizationServiceImpl organizationService, ApplicationServiceImpl applicationService, RestTemplate restTemplate, ActivityRepository activityRepository,MessageSourceImpl messageSource,LastEvaluatedRepository lastEvaluatedRepository) {
        this.mapper = mapper;
        this.tokenService = tokenService;
        this.organizationService = organizationService;
        this.applicationService = applicationService;
        this.restTemplate = restTemplate;
        this.activityRepository = activityRepository;
        this.messageSource=messageSource;
        this.lastEvaluatedRepository=lastEvaluatedRepository;
    }

    public void fetchAndSaveActivities() throws EntityNotFound {
        log.info("Application activity schedulers executed.");
        List<OrganizationDTO> organizations = organizationService.getOrganizations();

        String startFormatted=DateUtil.startOfPreviousDay().toString()+"&";
        String endFormatted=DateUtil.endOfPreviousDay().toString();

        tokenService.refreshToken();
        String token = tokenService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {
            for (OrganizationDTO organization : organizations) {
                Long page=lastEvaluatedRepository.fetch(schedulerName,organization.getOrganizationId())==null?null:lastEvaluatedRepository.fetch("activity",organization.getOrganizationId()).getLastKey();
                do {
                    String finalUrl = baseUrl + fetchOrganizationUrl + "/"
                            + organization.getOrganizationId().toString() + "/"
                            + fetchActivityDaily + "?date[start]=" + startFormatted + "date[stop]=" + endFormatted+ "&page_limit=10";

                    if(page!=null)
                    {
                        finalUrl+="&page_start_id="+page.toString();
                    }

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
                    page=response.getBody().getPage()==null?null:response.getBody().getPage().getPageStartId();

                    for (ActivityDTO activity : activities) {
                        activity.setOrganizationId(organization.getOrganizationId());
                        activityRepository.save(mapper.map(activity, Activity.class));
                        applicationService.save(mapper.map(activity, ApplicationDTO.class),activity);
                    }
                    lastEvaluatedRepository.save(new LastEvaluated(schedulerName,organization.getOrganizationId(),page));
                }while (page!=null);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException( messageSource.getMessage("Hubstaff.api.error") + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("failed.to.connectHubstaff", 503, e);
        }
    }

    public ApplicationActivityDTO getTotalTimeSpentOnAppByProject(ActivityDTO activityDTO) {
        Long projectId=activityDTO.getProjectId();
        String appName=activityDTO.getAppName();

        List<Activity> timeSpent= activityRepository.getTotalTimeSpentOnAppByProject(projectId,appName);

        if(ObjectUtil.isNullOrEmpty(timeSpent))
        {
            return null;
        }

        int totalTracked = timeSpent.stream()
                .reduce(0, (sum, activity) -> sum + activity.getTracked(), Integer::sum);

        return new ApplicationActivityDTO("Total time spend by project: "+projectId+" on app "+appName+" is: "+totalTracked,totalTracked);
    }

    public ApplicationActivityDTO getTotalTimeSpentOnAppByOrganization(ActivityDTO activityDTO) {
        Integer organizationId=activityDTO.getOrganizationId();
        String appName=activityDTO.getAppName();
        List<Activity> timeSpent= activityRepository.getTotalTimeSpentOnAppByOrganization(organizationId,appName);

        if(ObjectUtil.isNullOrEmpty(timeSpent))
        {
            return null;
        }

        int totalTracked = timeSpent.stream()
                .reduce(0, (sum, activity) -> sum + activity.getTracked(), Integer::sum);

        return new ApplicationActivityDTO("Total time spend by organization with id: "+organizationId+" on app "+appName+" is: ",totalTracked);
    }

    public ApplicationActivityDTO getTotalTimeSpentOnApp(ActivityDTO activityDTO) {
        Integer userId=activityDTO.getUserId();
        String appName=activityDTO.getAppName();
        List<Activity> timeSpent= activityRepository.getTotalTimeSpentOnApp(userId,appName);
        if(ObjectUtil.isNullOrEmpty(timeSpent))
        {
            return null;
        }

        int totalTracked = timeSpent.stream()
                .reduce(0, (sum, activity) -> sum + activity.getTracked(), Integer::sum);

        return new ApplicationActivityDTO("Total time spend by user "+userId+" on app "+appName+" is: "+totalTracked,totalTracked);
    }

    public List<ActiveUserResponse> getMostActiveUsers(Integer organizationId){
        String startDate=DateUtil.startOfCurrentMonth().toString();
        String endDate=DateUtil.endOfCurrentMonth().toString();

        List<Activity> activities=activityRepository.getMonthActivity(organizationId,startDate,endDate);

        Map<Long ,Integer> map=new HashMap<>();

        for(Activity activity:activities)
        {
            map.merge(activity.getUserId(), activity.getTracked(), Integer::sum);
        }
        ArrayList<ActiveUserResponse> users=new ArrayList<>();
        List<Map.Entry<Long,Integer>> result=map.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(5).toList();
        for (Map.Entry<Long,Integer> row:result)
        {
            users.add(new ActiveUserResponse(row.getKey(),row.getValue()));
        }
        return users;
    }

    public List<AppsWithTrendResponse> getAppUsedThisMonthWithTrend(Integer organizationId) {
        String startDateOfPreviousMonth=DateUtil.startOfPreviousMonth().toString();
        String endDateOfPreviousMonth=DateUtil.endOfPreviousMonth().toString();

        List<String> applications=applicationService.getAppsUsedCurrentMonth(organizationId);
        HashSet<String> thisMonthApplications=new HashSet<>(applications);

        List<Activity> activities=activityRepository.getMonthActivity(organizationId,startDateOfPreviousMonth,endDateOfPreviousMonth);

        HashSet<String> previousMonthApplications=new HashSet<>(activities.stream().map(Activity::getAppName).toList());

        String trend;

        if(thisMonthApplications.size()==previousMonthApplications.size())
        {
            trend="SAME";
        }
        else if(thisMonthApplications.size() > previousMonthApplications.size())
        {
            trend="UP";
        }
        else {
            trend="DOWN";
        }

        List<AppsWithTrendResponse> result=new ArrayList<>();
        result.add(new AppsWithTrendResponse(thisMonthApplications,trend));
        return result;
    }

    public List<TrendOfTopFiveApps> getTopFiveApps(Integer organizationId)
    {
        String startOfCurrentMonth=DateUtil.startOfCurrentMonth().toString();
        String endOfCurrentMonth=DateUtil.endOfCurrentMonth().toString();

        String startOfPreviousMonth=DateUtil.startOfPreviousMonth().toString();
        String endOfPreviousMonth=DateUtil.endOfPreviousMonth().toString();

        List<Activity> thisMonthActivities =activityRepository.getMonthActivity(organizationId,startOfCurrentMonth,endOfCurrentMonth);
        List<Activity> previousMonthActivities =activityRepository.getMonthActivity(organizationId,startOfPreviousMonth,endOfPreviousMonth);

        Map<String , Integer> thisMonthActivity=new HashMap<>();
        Map<String , Integer> previousMonthActivity=new HashMap<>();

        for(Activity activity: thisMonthActivities)
        {
            thisMonthActivity.merge(activity.getAppName(), activity.getTracked(), Integer::sum);
        }

        for (Activity activity:previousMonthActivities)
        {
            previousMonthActivity.merge(activity.getAppName(), activity.getTracked(), Integer::sum);
        }

        List<TrendOfTopFiveApps> result=new ArrayList<>();

        List<Map.Entry<String , Integer>> topFiveApps= thisMonthActivity.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .toList();

        for(Map.Entry<String , Integer> app:topFiveApps)
        {
            String appName=app.getKey();
            Integer thisMonthTotal=app.getValue();
            Integer previousMonthTotal=previousMonthActivity.get(appName);
            String trend;

            if(previousMonthTotal==null)
            {
                result.add(new TrendOfTopFiveApps(appName,thisMonthTotal,"UP"));
                continue;
            }
            if(thisMonthTotal.equals(previousMonthTotal))
            {
                trend="SAME";
            }
            else if (thisMonthTotal<previousMonthTotal)
            {
                trend="DOWN";
            }
            else
            {
                trend="UP";
            }
            result.add(new TrendOfTopFiveApps(appName,thisMonthTotal,trend));
        }
        return result;
    }
}


