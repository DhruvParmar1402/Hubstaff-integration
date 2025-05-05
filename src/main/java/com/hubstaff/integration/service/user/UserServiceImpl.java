package com.hubstaff.integration.service.user;

import com.hubstaff.integration.dto.OrganizationDTO;
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
import com.hubstaff.integration.util.DateUtil;
import com.hubstaff.integration.util.MessageSourceImpl;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrganizationService organizationService;
    private final TokenService tokenService;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    private final MessageSourceImpl messageSource;
    private final CollectionsUtil<UserDTO> util = new CollectionsUtil<>();

    public UserServiceImpl(UserRepository userRepository, OrganizationServiceImpl organizationService, TokenServiceImpl tokenService, RestTemplate restTemplate, ModelMapper mapper,MessageSourceImpl messageSource) {
        this.userRepository = userRepository;
        this.organizationService = organizationService;
        this.tokenService = tokenService;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.messageSource=messageSource;
    }

    @Value("${base.api.url}")
    private String baseUrl;

    @Value("${fetch.organization.url}")
    private String fetchOrganizationUrl;

    @Value("${fetch.users.url}")
    private String fetchUserUrl;

    @Override
    public void fetchAndSaveUsers() throws EntityNotFound {
        List<OrganizationDTO> organizations = organizationService.getOrganizations();
        String token = tokenService.getAccessToken();

        if (ObjectUtil.isNullOrEmpty(organizations)) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        tokenService.refreshToken();

        try {
            Long page=null;
            for (OrganizationDTO organization : organizations) {
                String finalUrl = baseUrl + fetchOrganizationUrl + "/" + organization.getOrganizationId() + fetchUserUrl;

                if(page!=null)
                {
                    finalUrl+="&page_start_id="+page.toString();
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
            throw new ExternalApiException(messageSource.getMessage("Hubstaff.api.error") + e.getStatusText(), e.getStatusCode().value(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("failed.to.connectHubstaff", 503, e);
        } catch (Exception e) {
            throw new ExternalApiException("Hubstaff.unexpected.error", 500, e);
        }
    }

    @Override
    public List<UserDTO> getUsers(Integer organizationId) {
        List<User>users=userRepository.findUserByOrganization(organizationId);
        if(ObjectUtil.isNullOrEmpty(users))
        {
            return new ArrayList<>();
        }
        return users.stream().map(user -> mapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public List<UserDTO> getNewUsers(Long organizationId) {
        String startOfMonth= DateUtil.startOfCurrentMonth().toString();
        String endOfMonth=DateUtil.endOfCurrentMonth().toString();

        List<User> users=userRepository.findUserByOrganizationId(organizationId,startOfMonth,endOfMonth);
        return users.stream().map(entity->mapper.map(entity,UserDTO.class)).toList();
    }
}
