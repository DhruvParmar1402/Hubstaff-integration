package com.hubstaff.integration.controller;

import com.hubstaff.integration.Validations.groups;
import com.hubstaff.integration.dto.ActivityDTO;
import com.hubstaff.integration.dto.ApplicationActivityDTO;
import com.hubstaff.integration.service.activity.ActivityServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ApplicationActivityController{

    private final ActivityServiceImpl activityService;
    private final MessageSourceImpl messageSource;

    public ApplicationActivityController(MessageSourceImpl messageSource,ActivityServiceImpl activityService)
    {
        this.messageSource=messageSource;
        this.activityService=activityService;
    }

    @PostMapping("/timeSpent/user")
    public ResponseEntity<?> getTimeSpentOnAppByUser(@Validated(groups.fetchActivityByUser.class) @RequestBody ActivityDTO activityDTO)
    {
        ResponseHandler<ApplicationActivityDTO> response;
        try {
            ApplicationActivityDTO activity= activityService.getTotalTimeSpentOnApp(activityDTO);
            response=new ResponseHandler<>(activity,messageSource.getMessage("total.time.spent"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/timeSpent/project")
    public ResponseEntity<?> getTimeSpentOnAppByProject(@Validated(groups.fetchActivityByProject.class) @RequestBody ActivityDTO activityDTO)
    {
        ResponseHandler<ApplicationActivityDTO> response;
        try {
            ApplicationActivityDTO activity= activityService.getTotalTimeSpentOnAppByProject(activityDTO);
            response=new ResponseHandler<>(activity,messageSource.getMessage("total.time.spent"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/timeSpent/organization")
    public ResponseEntity<?> getTimeSpentOnAppByOrganization(@Validated(groups.fetchActivityByOrganization.class) @RequestBody ActivityDTO activityDTO)
    {
        ResponseHandler<ApplicationActivityDTO> response;
        try {
            ApplicationActivityDTO activity= activityService.getTotalTimeSpentOnAppByOrganization(activityDTO);
            response=new ResponseHandler<>(activity,messageSource.getMessage("total.time.spent"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
