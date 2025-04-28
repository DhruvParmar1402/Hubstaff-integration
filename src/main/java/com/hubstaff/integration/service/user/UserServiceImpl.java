package com.hubstaff.integration.service.user;

import com.hubstaff.integration.dto.OrganizationDTO;
import com.hubstaff.integration.dto.PaginationResponse;
import com.hubstaff.integration.dto.UserDTO;
import com.hubstaff.integration.dto.UserResponse;
import com.hubstaff.integration.entity.User;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.exception.ExternalApiException;
import com.hubstaff.integration.repository.UserRepository;
import com.hubstaff.integration.service.organization.OrganizationService;
import com.hubstaff.integration.service.organization.OrganizationServiceImpl;
import com.hubstaff.integration.service.token.TokenService;
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

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrganizationService organizationServiceImpl;
    private final TokenService tokenServiceImpl;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    private final CollectionsUtil<UserDTO> util = new CollectionsUtil<>();

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

    public void fetchAndSaveUsers() throws EntityNotFound {
        List<OrganizationDTO> organizations = organizationServiceImpl.getOrganizations();
        String token = tokenServiceImpl.getAccessToken();

        if (ObjectUtil.isNullOrEmpty(organizations)) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {
            PaginationResponse page=null;
            for (OrganizationDTO organization : organizations) {
                String finalUrl = baseUrl + fetchOrganizationUrl + "/" + organization.getOrganizationId() + fetchUserUrl;

                if(page!=null)
                {
                    finalUrl+="&page_start_id="+page.getNextPageStartId().toString();
                }

                ResponseEntity<UserResponse> response = restTemplate.exchange(
                        finalUrl,
                        HttpMethod.GET,
                        requestEntity,
                        UserResponse.class
                );

                if (ObjectUtil.isNullOrEmpty(response.getBody()) || ObjectUtil.isNullOrEmpty(response.getBody().getUsers())) {
                    continue;
                }

                List<UserDTO> users = response.getBody().getUsers();

                page=response.getBody().getPage();

                users = util.filterFromPreviousDay(users, UserDTO::getCreatedAt);

                for (UserDTO user : users) {
                    if (!ObjectUtil.validateDto(user)) {
                        continue;
                    }

                    user.setOrganizationId(organization.getOrganizationId());
                    user.setOrganizationName(organization.getOrganizationName());
                    userRepository.save(mapper.map(user, User.class));
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
        List<User>users=userRepository.findUserByOrganization(organizationName);
        if(ObjectUtil.isNullOrEmpty(users))
        {
            return null;
        }
        return users.stream().map(user -> mapper.map(user, UserDTO.class)).toList();
    }
}
