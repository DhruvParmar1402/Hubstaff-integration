package com.hubstaff.integration.controller;

import com.hubstaff.integration.dto.IntegrationDTO;
import com.hubstaff.integration.service.token.TokenService;
import com.hubstaff.integration.service.token.TokenServiceImpl;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenService tokenService;
    private final MessageSourceImpl messageSource;

    public TokenController(TokenServiceImpl tokenService, MessageSourceImpl messageSource)
    {
        this.tokenService = tokenService;
        this.messageSource=messageSource;
    }

    @GetMapping("/code")
    public ResponseEntity<?> generateCode ()
    {
        ResponseHandler<String> response;
        try {
            tokenService.getCode();
            response=new ResponseHandler<>(null,messageSource.getMessage("oauth.initiated"),HttpStatus.OK,true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            response=new ResponseHandler<>(null, e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<?> saveTokens(@RequestParam String code){
        ResponseHandler<IntegrationDTO> response;
        try {
            IntegrationDTO integrationDTO= tokenService.getTokens(code);
            response=new ResponseHandler<>(integrationDTO, messageSource.getMessage("token.save.success"), HttpStatus.OK,true);
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
