package com.hubstaff.integration.service.token;

import com.hubstaff.integration.dto.IntegrationDTO;
import com.hubstaff.integration.exception.EntityNotFound;

public interface TokenService {
    void getCode();
    IntegrationDTO getTokens(String code);
    String getAccessToken() throws EntityNotFound;
    IntegrationDTO getToken();
    void refreshToken();
    void refreshAccessToken(IntegrationDTO integrationDTO);
}
