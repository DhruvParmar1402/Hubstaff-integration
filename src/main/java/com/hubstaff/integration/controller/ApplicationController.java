package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.ApplicationDTO;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.service.application.ApplicationServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationServiceImpl applicationServiceImpl;
    private final MessageSourceImpl messageSource;

    public ApplicationController(ApplicationServiceImpl applicationService,MessageSourceImpl messageSource)
    {
        this.applicationServiceImpl=applicationService;
        this.messageSource=messageSource;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getApplication (@PathVariable @NotNull Integer userId)
    {
        ResponseHandler<List<ApplicationDTO>> response;
        try {
            List<ApplicationDTO>applicationDTOS = applicationServiceImpl.fetchByUserId(userId);
            response=new ResponseHandler<>(applicationDTOS,messageSource.getMessage("userApp.fetch.success"), HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (EntityNotFound e)
        {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.BAD_REQUEST,true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(),HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
