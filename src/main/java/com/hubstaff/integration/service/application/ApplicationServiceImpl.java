package com.hubstaff.integration.service.application;

import com.hubstaff.integration.dto.ActivityDTO;
import com.hubstaff.integration.dto.ApplicationDTO;
import com.hubstaff.integration.entity.Application;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.repository.ApplicationRepository;
import com.hubstaff.integration.util.DateUtil;
import com.hubstaff.integration.util.ObjectUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ModelMapper mapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ModelMapper modelMapper)
    {
        this.applicationRepository=applicationRepository;
        this.mapper=modelMapper;
    }

    public void save(ApplicationDTO applicationDTO, ActivityDTO activityDTO)
    {
        List<Application> toBeStored=applicationRepository.fetchByUserIdAndAppName(activityDTO.getUserId(),activityDTO.getAppName());

        if(ObjectUtil.isNullOrEmpty(toBeStored))
        {
            applicationDTO.setAddedAt(activityDTO.getCreatedAt());
        }
        else {
            applicationDTO.setAddedAt(toBeStored.getFirst().getAddedAt());
        }

        applicationDTO.setLastUsedAt(activityDTO.getCreatedAt());

        applicationRepository.save(mapper.map(applicationDTO, Application.class));
    }

    public List<ApplicationDTO> fetchByUserId(Integer userId) throws EntityNotFound {
        List<Application>applications=applicationRepository.fetchByUserIdAndAppName(userId,null);
        if (ObjectUtil.isNullOrEmpty(applications))
        {
            throw new EntityNotFound("application.not.exists");
        }
        return applications.stream().map(entity->mapper.map(entity, ApplicationDTO.class)).toList();
    }

    public List<String> fetchNewApps(Integer organizationId) throws EntityNotFound {
        String startOfThisMonth=DateUtil.startOfCurrentMonth().toString();
        String endOfThisMonth=DateUtil.endOfCurrentMonth().toString();

        String startOfPreviousMonth= DateUtil.startOfPreviousMonth().toString();
        String endOfPreviousMonth=DateUtil.endOfPreviousMonth().toString();


        List<Application> appsUsedThisMonth=applicationRepository.fetchNewApplications(organizationId,startOfThisMonth,endOfThisMonth);

        HashSet<String> appsUsedPreviousMonth=new HashSet<>(applicationRepository.fetchNewApplications(organizationId,startOfPreviousMonth,endOfPreviousMonth).stream().map(Application::getAppName).toList());

        List<String> result=new ArrayList<>();

        for(Application apps:appsUsedThisMonth)
        {
            if(!appsUsedPreviousMonth.contains(apps.getAppName()))
            {
                result.add(apps.getAppName());
            }
        }

        HashSet<String> uniqueApplicationsThisMonth=new HashSet<>(result);

        if (ObjectUtil.isNullOrEmpty(uniqueApplicationsThisMonth))
        {
            throw new EntityNotFound("application.not.exists");
        }

        return uniqueApplicationsThisMonth.stream().toList();
    }

    public List<Map.Entry<String , Integer>> getTopFiveApps(Integer organizationId) throws EntityNotFound {
        String startOfMonth=DateUtil.startOfCurrentMonth().toString();
        String endOfMonth=DateUtil.endOfCurrentMonth().toString();

        Map<String ,Integer> map=new HashMap<>();

        List<Application> applications=applicationRepository.getAppsUsedBetweenStartAndEnd(organizationId,startOfMonth,endOfMonth);

        if(ObjectUtil.isNullOrEmpty(applications))
        {
            throw new EntityNotFound("applications.not.exists");
        }

        for (Application application:applications)
        {
            if(!map.containsKey(application.getAppName()))
            {
                map.put(application.getAppName(),1);
            }
            else
            {
                Integer count = map.get(application.getAppName());
                map.put(application.getAppName(),count+1);
            }
        }

        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .toList();
    }

    public List<String> getAppsUsedCurrentMonth (Integer organizationId)
    {
        String startOfMonth=DateUtil.startOfCurrentMonth().toString();
        String endOfMonth=DateUtil.endOfCurrentMonth().toString();

        List<Application> applications=applicationRepository.getAppsUsedBetweenStartAndEnd(organizationId,startOfMonth,endOfMonth);

        return applications.stream().map(Application::getAppName).toList();
    }
}
