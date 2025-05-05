package com.hubstaff.integration.service.token;

import com.hubstaff.integration.dto.*;
import com.hubstaff.integration.entity.Integration;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.exception.ExternalApiException;
import com.hubstaff.integration.repository.TokenRepository;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ObjectUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${authorization.code.url}")
    private String getCodeUrl;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${redirect.url}")
    private String redirectUrl;

    @Value("${authorization.code.url}")
    private String authorizationCodeUrl;

    @Value("${authorization.token.url}")
    private String tokenUrl;

    @Value("${code.obtain.grantType}")
    private String codeObtainGrantType;

    @Value("${accessToken.obtain.grantType}")
    private String accessTokenObtainGrantType;

    @Value("${accessToken.refresh.grantType}")
    private String accessTokenRefreshGrantType;

    private final RestTemplate restTemplate;
    private final TokenRepository tokenRepository;
    private final ModelMapper mapper;
    private final MessageSourceImpl messageSource;
    private final Logger logger= LoggerFactory.getLogger(TokenServiceImpl.class);

    public TokenServiceImpl(RestTemplate restTemplate, TokenRepository tokenRepository, ModelMapper modelMapper, MessageSourceImpl messageSource)
    {
        this.restTemplate=restTemplate;
        this.tokenRepository=tokenRepository;
        this.mapper=modelMapper;
        this.messageSource=messageSource;
    }

    public void getCode() {
        String scope = "hubstaff:read";

        String finalUrl = authorizationCodeUrl + "?client_id=" + clientId
                + "&response_type=" +codeObtainGrantType
                + "&nonce="+ UUID.randomUUID().toString()
                + "&redirect_uri=" + redirectUrl
                + "&scope=" + scope;
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + finalUrl);
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + finalUrl);
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("xdg-open " + finalUrl);
            } else {
                logger.error("os not supported");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IntegrationDTO getTokens(String code) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", accessTokenObtainGrantType);
        formData.add("code", code);
        formData.add("redirect_uri", redirectUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<TokenDTO> response;

        try {
            response = restTemplate.postForEntity(tokenUrl, request, TokenDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException(messageSource.getMessage("Hubstaff.api.error") + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("failed.to.connectHubstaff", 503, e);
        }

        Integration entity=mapper.map(response.getBody(), Integration.class);
        entity.setClientId(clientId);
        entity.setClientSecret(clientSecret);
        entity.setRedirectUri(redirectUrl);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());

        tokenRepository.save(entity);
        return mapper.map(entity, IntegrationDTO.class);
    }

    public String getAccessToken() throws EntityNotFound {
        String token=tokenRepository.getAccessToken(clientId);
        if(!ObjectUtil.validateDto(token))
        {
            throw new EntityNotFound("token.not.exists");
        }
        return token;
    }

    public IntegrationDTO getToken()
    {
        Integration integration=tokenRepository.getRefreshToken(clientId);
        return integration==null?null:mapper.map(integration,IntegrationDTO.class);
    }

    public void refreshToken() {
        IntegrationDTO integrationDTO = getToken();
        if (!ObjectUtil.validateDto(integrationDTO)) {
                return;
        }

        Instant createdAt=integrationDTO.getCreatedAt().toInstant();
        int expiresAfter=Integer.parseInt(integrationDTO.getExpiresIn());

        Instant accessExpiresAt=createdAt.plusSeconds((long)expiresAfter-3660);
        boolean isAccessExpired= !Instant.now().isBefore(accessExpiresAt);

        Instant refreshExpiresAt =createdAt.plusSeconds( (long)29 * 24 * 60 * 60);
        boolean isRefreshExpired= !Instant.now().isBefore(refreshExpiresAt);

        if(isAccessExpired || isRefreshExpired)
        {
            refreshAccessToken(integrationDTO);
        }
    }

    public void refreshAccessToken(IntegrationDTO integrationDTO) {
        if (!ObjectUtil.validateDto(integrationDTO)) {
            return;
        }
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", accessTokenRefreshGrantType);
        formData.add("refresh_token", integrationDTO.getRefreshToken());
        formData.add("scope", "hubstaff:read");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<TokenDTO> response;

        try {
            response = restTemplate.postForEntity(tokenUrl, request, TokenDTO.class);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException(messageSource.getMessage("Hubstaff.api.error") + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("failed.to.connectHubstaff", 503, e);
        } catch (Exception e) {
            throw new ExternalApiException("Hubstaff.unexpected.error", 500, e);
        }

        Integration entity=mapper.map(response.getBody(), Integration.class);
        entity.setClientId(clientId);
        entity.setClientSecret(clientSecret);
        entity.setRedirectUri(redirectUrl);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());

        tokenRepository.save(entity);
    }
}
