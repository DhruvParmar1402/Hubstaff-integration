package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.UserDTO;
import com.hubstaff.integration.service.user.UserService;
import com.hubstaff.integration.service.user.UserServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final MessageSourceImpl messageSource;

    public UserController(UserServiceImpl userService, MessageSourceImpl messageSource)
    {
        this.userService = userService;
        this.messageSource=messageSource;
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<?> getUsers (@PathVariable Integer organizationId)
    {
        ResponseHandler<List<UserDTO>> response;
        try {
            List<UserDTO>users= userService.getUsers(organizationId);
            response=new ResponseHandler<>(users,messageSource.getMessage("users.fetched.success"), HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    6
    @GetMapping("/new/{organizationId}")
    public ResponseEntity<?> getNewUsers(@PathVariable Long organizationId)
    {
        ResponseHandler<List<UserDTO>> response;
        try {
            List<UserDTO> users=userService.getNewUsers(organizationId);
            response=new ResponseHandler<>(users,messageSource.getMessage("user.fetch.success"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
