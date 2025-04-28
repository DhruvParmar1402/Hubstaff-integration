package com.hubstaff.integration.service.token;

import com.hubstaff.integration.dto.IntegrationDTO;

public interface TokenService {
    void getCode();
    IntegrationDTO getTokens(String code);
    String getAccessToken();
    IntegrationDTO getToken();
    void refreshToken();
    void refreshAccessToken();
}
