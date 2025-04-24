package com.hubstaff.integration.service.user;

import com.hubstaff.integration.dto.OrganizationDTO;
import com.hubstaff.integration.dto.UserDTO;
import com.hubstaff.integration.dto.UserResponse;
import com.hubstaff.integration.entity.UserEntity;
import com.hubstaff.integration.exception.ExternalApiException;
import com.hubstaff.integration.repository.UserRepository;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import com.hubstaff.integration.service.token.TokenServiceImpl;
import com.hubstaff.integration.util.CollectionsUtil;
import com.hubstaff.integration.util.ObjectUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userRepository;
    private final OrganizationServiceImpl organizationServiceImpl;
    private final TokenServiceImpl tokenServiceImpl;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    private CollectionsUtil<UserDTO> util = new CollectionsUtil<>();

    public UserServiceImpl(UserRepository userRepository, OrganizationServiceImpl organizationServiceImpl, TokenServiceImpl tokenServiceImpl, RestTemplate restTemplate, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.organizationServiceImpl = organizationServiceImpl;
        this.tokenServiceImpl = tokenServiceImpl;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @Value("${base.api.url}")
    private String baseUrl;

    @Value("${fetch.organization.url}")
    private String fetchOrganizationUrl;

    @Value("${fetch.users.url}")
    private String fetchUserUrl;

    public void fetchAndSaveUsers() {
        List<OrganizationDTO> organizations = organizationServiceImpl.getOrganizations();
        String token = tokenServiceImpl.getAccessToken();

        // Defensive check on token and organizations
        if (ObjectUtil.isNullOrEmpty(token) || ObjectUtil.isNullOrEmpty(organizations)) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {
            for (OrganizationDTO organization : organizations) {

                // Null-safety check for OrganizationDTO content
                if (ObjectUtil.isNullOrEmpty(organization.getOrganizationId())) {
                    continue;
                }

                String finalUrl = baseUrl + fetchOrganizationUrl + "/" + organization.getOrganizationId() + fetchUserUrl;

                ResponseEntity<UserResponse> response = restTemplate.exchange(
                        finalUrl,
                        HttpMethod.GET,
                        requestEntity,
                        UserResponse.class
                );

                // Check for null body or null/empty user list
                if (ObjectUtil.isNullOrEmpty(response.getBody()) || ObjectUtil.isNullOrEmpty(response.getBody().getUsers())) {
                    continue;
                }

                List<UserDTO> users = response.getBody().getUsers();

                // Optional: filter based on creation date if required
                users = util.filterFromPreviousDay(users, UserDTO::getCreatedAt);

                for (UserDTO user : users) {
                    if (!ObjectUtil.validateDto(user)) {
                        continue; // skip invalid user DTOs
                    }

                    user.setOrganizationId(organization.getOrganizationId());
                    user.setOrganizationName(organization.getOrganizationName());
                    userRepository.save(mapper.map(user, UserEntity.class));
                }
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalApiException("Hubstaff API error: " + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Failed to connect to Hubstaff API", 503, e);
        } catch (Exception e) {
            throw new ExternalApiException("Unexpected error while calling Hubstaff API", 500, e);
        }
    }


    public List<UserDTO> getUsers(String organizationName) {
        return userRepository.findUserByOrganization(organizationName).stream().map(userEntity -> mapper.map(userEntity, UserDTO.class)).toList();
    }
}
