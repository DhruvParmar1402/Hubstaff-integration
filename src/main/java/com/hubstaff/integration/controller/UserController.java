package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.UserDTO;
import com.hubstaff.integration.service.user.UserServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final MessageSourceImpl messageSource;

    public UserController(UserServiceImpl userServiceImpl, MessageSourceImpl messageSource)
    {
        this.userServiceImpl = userServiceImpl;
        this.messageSource=messageSource;
    }

    @GetMapping("/{organizationName}")
    public ResponseEntity<?> getUsers (@PathVariable String organizationName)
    {
        ResponseHandler<List<UserDTO>> response;
        try {
            List<UserDTO>users= userServiceImpl.getUsers(organizationName);
            response=new ResponseHandler<>(users,messageSource.getMessage("users.fetched.success"), HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            response=new ResponseHandler<>(null,e.getMessage(), HttpStatus.BAD_REQUEST,false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
