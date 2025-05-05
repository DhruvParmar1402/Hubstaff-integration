package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.AppsWithTrendResponse;
import com.hubstaff.integration.dto.TrendOfTopFiveApps;
import com.hubstaff.integration.service.activity.ActivityService;
import com.hubstaff.integration.validations.Groups;
import com.hubstaff.integration.dto.ActivityDTO;
import com.hubstaff.integration.dto.ApplicationActivityDTO;
import com.hubstaff.integration.service.activity.ActivityServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/activities")
public class ApplicationActivityController{

    private final ActivityService activityService;
    private final MessageSourceImpl messageSource;

    public ApplicationActivityController(MessageSourceImpl messageSource,ActivityServiceImpl activityServiceImpl)
    {
        this.messageSource=messageSource;
        this.activityService=activityServiceImpl;
    }

    @PostMapping("/timeSpent/user")
    public ResponseEntity<?> getTimeSpentOnAppByUser(@Validated(Groups.FetchActivityByUser.class) @RequestBody ActivityDTO activityDTO)
    {
        ResponseHandler<ApplicationActivityDTO> response;
        try {
            ApplicationActivityDTO activity= activityService.getTotalTimeSpentOnApp(activityDTO);
            response=new ResponseHandler<>(activity,messageSource.getMessage("total.time.spent"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/timeSpent/project")
    public ResponseEntity<?> getTimeSpentOnAppByProject(@Validated(Groups.FetchActivityByProject.class) @RequestBody ActivityDTO activityDTO)
    {
        ResponseHandler<ApplicationActivityDTO> response;
        try {
            ApplicationActivityDTO activity= activityService.getTotalTimeSpentOnAppByProject(activityDTO);
            response=new ResponseHandler<>(activity,messageSource.getMessage("total.time.spent"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/timeSpent/organization")
    public ResponseEntity<?> getTimeSpentOnAppByOrganization(@Validated(Groups.FetchActivityByOrganization.class) @RequestBody ActivityDTO activityDTO)
    {
        ResponseHandler<ApplicationActivityDTO> response;
        try {
            ApplicationActivityDTO activity= activityService.getTotalTimeSpentOnAppByOrganization(activityDTO);
            response=new ResponseHandler<>(activity,messageSource.getMessage("total.time.spent"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    2
    @GetMapping("/mostActiveUser/{organizationId}")
    public ResponseEntity<?> getMostActive(@PathVariable Integer organizationId)
    {
        ResponseHandler<List<Map.Entry<Long , Integer>>> response;
        try {
            List<Map.Entry<Long, Integer>> users=activityService.getMostActiveUsers(organizationId);
            response=new ResponseHandler<>(users,messageSource.getMessage("mostActive.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    1
    @GetMapping("/getTopFiveApps/{organizationId}")
    public ResponseEntity<?> getTopFiveApps(@PathVariable Integer organizationId)
    {
        ResponseHandler<List<TrendOfTopFiveApps>> response;
        try {
            List<TrendOfTopFiveApps> users=activityService.getTopFiveApps(organizationId);
            response=new ResponseHandler<>(users,messageSource.getMessage("mostActive.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    4
    @GetMapping("/appsUsed/{organizationId}")
    public ResponseEntity<?> getAppUsedThisMonthWithTrend(@PathVariable Integer organizationId)
    {
        ResponseHandler<AppsWithTrendResponse> response;
        try {
            AppsWithTrendResponse appsWithTrendResponse=activityService.getAppUsedThisMonthWithTrend(organizationId);
            response=new ResponseHandler<>(appsWithTrendResponse,messageSource.getMessage("apps.fetched.success"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
