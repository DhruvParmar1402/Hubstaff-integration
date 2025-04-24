package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.OrganizationDTO;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationServiceImpl organizationServiceImpl;
    private final MessageSourceImpl messageSource;

    public OrganizationController(OrganizationServiceImpl organizationServiceImpl, MessageSourceImpl messageSource)
    {
        this.organizationServiceImpl = organizationServiceImpl;
        this.messageSource=messageSource;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrganizations(){
        ResponseHandler<Object> response;
        try
        {
            List<OrganizationDTO>organizations= organizationServiceImpl.getOrganizations();
            response=new ResponseHandler<>(organizations, messageSource.getMessage("organizations.fetched.success"), HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
