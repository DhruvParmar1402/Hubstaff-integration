package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.OrganizationDTO;
import com.hubstaff.integration.service.organization.OrganizationService;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final MessageSourceImpl messageSource;

    public OrganizationController(OrganizationServiceImpl organizationService, MessageSourceImpl messageSource)
    {
        this.organizationService = organizationService;
        this.messageSource=messageSource;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrganizations(){
        ResponseHandler<Object> response;
        try
        {
            List<OrganizationDTO>organizations= organizationService.getOrganizations();
            response=new ResponseHandler<>(organizations, messageSource.getMessage("organizations.fetched.success"), HttpStatus.OK,true);
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
