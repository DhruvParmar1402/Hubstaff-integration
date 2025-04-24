package com.hubstaff.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class IntegrationDTO {

    public IntegrationDTO()
    {
        this.createdAt=new Date();
        this.updatedAt=new Date();
    }

    private String clientId;

    private String clientSecret;

    private String accessToken;

    private String refreshToken;

    private String redirectUri;

    private String expiresIn;

    private Date createdAt;

    private Date updatedAt;
}
