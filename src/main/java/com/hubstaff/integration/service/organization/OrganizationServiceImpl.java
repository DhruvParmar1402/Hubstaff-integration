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
import com.hubstaff.integration.util.ObjectUtil;
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

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Value("${base.api.url}")
    private String baseUrl;

    @Value("${fetch.organization.url}")
    private String fetchOrganizationUrl;

    private TokenService tokenServiceImpl;
    private ModelMapper mapper;
    private RestTemplate restTemplate;
    private OrganizationRepository organizationRepository;
    private final CollectionsUtil<OrganizationDTO> util = new CollectionsUtil<>();

    public OrganizationServiceImpl(TokenServiceImpl tokenServiceImpl, ModelMapper modelMapper, RestTemplate restTemplate, OrganizationRepository organizationRepository)
    {
        this.tokenServiceImpl = tokenServiceImpl;
        this.mapper=modelMapper;
        this.restTemplate=restTemplate;
        this.organizationRepository=organizationRepository;
    }

    public void fetchAndSave() throws EntityNotFound {
        String token = tokenServiceImpl.getAccessToken();
        if (ObjectUtil.isNullOrEmpty(token)) {
            throw new ExternalApiException("Access token is null or empty", 401, null);
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
            throw new ExternalApiException("Hubstaff API error: " + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Failed to connect to Hubstaff API", 503, e);
        } catch (Exception e) {
            throw new ExternalApiException("Unexpected error while calling Hubstaff API", 500, e);
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
