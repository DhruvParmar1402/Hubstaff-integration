package com.hubstaff.integration.service.organization;

import com.hubstaff.integration.dto.OrganizationDTO;
import com.hubstaff.integration.dto.OrganizationResponse;
import com.hubstaff.integration.entity.Organization;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.exception.ExternalApiException;
import com.hubstaff.integration.repository.OrganizationRepository;
import com.hubstaff.integration.service.token.TokenService;
import com.hubstaff.integration.service.token.TokenServiceImpl;
import com.hubstaff.integration.util.CollectionsUtil;
import com.hubstaff.integration.util.MessageSourceImpl;
import com.hubstaff.integration.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Value("${base.api.url}")
    private String baseUrl;

    @Value("${fetch.organization.url}")
    private String fetchOrganizationUrl;

    private TokenService tokenService;
    private ModelMapper mapper;
    private RestTemplate restTemplate;
    private OrganizationRepository organizationRepository;
    private final MessageSourceImpl messageSource;

    public OrganizationServiceImpl(TokenServiceImpl tokenService, ModelMapper modelMapper, RestTemplate restTemplate, OrganizationRepository organizationRepository,MessageSourceImpl messageSource)
    {
        this.tokenService = tokenService;
        this.mapper=modelMapper;
        this.restTemplate=restTemplate;
        this.organizationRepository=organizationRepository;
        this.messageSource=messageSource;
    }

    public void fetchAndSave() throws EntityNotFound {
        log.info("Organization scheduler executed.");

        tokenService.refreshToken();
        String token = tokenService.getAccessToken();
        CollectionsUtil<OrganizationDTO> util = new CollectionsUtil<>();

        if (ObjectUtil.isNullOrEmpty(token)) {
            throw new ExternalApiException("token.not.exists", 401, null);
        }

        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<OrganizationResponse> response;
        try {
            response = restTemplate.exchange(
                    baseUrl + fetchOrganizationUrl,
                    HttpMethod.GET,
                    requestEntity,
                    OrganizationResponse.class
            );
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException(messageSource.getMessage("Hubstaff.api.error") + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("failed.to.connectHubstaff", 503, e);
        } catch (Exception e) {
            throw new ExternalApiException("Hubstaff.unexpected.error", 500, e);
        }

        OrganizationResponse body = response.getBody();
        List<OrganizationDTO> organizations =
                body == null || ObjectUtil.isNullOrEmpty(body.getOrganizations())
                        ? new ArrayList<>()
                        : body.getOrganizations();

        organizations= util.filterFromPreviousDay(organizations, OrganizationDTO::getCreatedAt);

        for (OrganizationDTO organization : organizations) {
            organizationRepository.save(mapper.map(organization, Organization.class));
        }
    }

    public List<OrganizationDTO> getOrganizations() {
        List<Organization> organizations=organizationRepository.getOrganizations();
        if(ObjectUtil.isNullOrEmpty(organizations))
        {
            return new ArrayList<>();
        }
        return organizations.stream().map(organization -> mapper.map(organization,OrganizationDTO.class)).toList();
    }
}
