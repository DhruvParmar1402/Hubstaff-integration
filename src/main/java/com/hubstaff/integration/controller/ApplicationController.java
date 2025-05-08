package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.ApplicationDTO;
import java.util.Map;

import com.hubstaff.integration.dto.NewAppsResponse;
import com.hubstaff.integration.dto.Top5AppsThisMonthResponse;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.service.application.ApplicationService;
import com.hubstaff.integration.service.application.ApplicationServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final MessageSourceImpl messageSource;

    public ApplicationController(ApplicationServiceImpl applicationService,MessageSourceImpl messageSource)
    {
        this.applicationService=applicationService;
        this.messageSource=messageSource;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getApplication (@PathVariable Integer userId)
    {
        ResponseHandler<List<ApplicationDTO>> response;
        try {
            List<ApplicationDTO>applicationDTOS = applicationService.fetchByUserId(userId);
            response=new ResponseHandler<>(applicationDTOS,messageSource.getMessage("userApp.fetch.success"), HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (EntityNotFound e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.NOT_FOUND,true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    5
    @GetMapping("/new/{organizationId}")
    public ResponseEntity<?> getNewApplications (@PathVariable Integer organizationId)
    {
        ResponseHandler<List<NewAppsResponse>> response;
        try {
            List<NewAppsResponse> newApplications=applicationService.fetchNewApps(organizationId);
            response=new ResponseHandler<>(newApplications,messageSource.getMessage("newApps.fetch.success"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    3
    @GetMapping("/topFiveApps/{organizationId}")
    public ResponseEntity<?> getTopFiveApps(@PathVariable Integer organizationId)
    {
        ResponseHandler<List<Top5AppsThisMonthResponse>> response;
        try
        {
            List<Top5AppsThisMonthResponse> applications=applicationService.getTopFiveApps(organizationId);
            response=new ResponseHandler<>(applications,messageSource.getMessage("activeApps.fetch.success"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
