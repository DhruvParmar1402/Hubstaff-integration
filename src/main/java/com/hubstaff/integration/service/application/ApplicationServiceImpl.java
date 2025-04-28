package com.hubstaff.integration.service.application;

import com.hubstaff.integration.dto.ApplicationDTO;
import com.hubstaff.integration.entity.Application;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.repository.ApplicationRepository;
import com.hubstaff.integration.util.ObjectUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ModelMapper mapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ModelMapper modelMapper)
    {
        this.applicationRepository=applicationRepository;
        this.mapper=modelMapper;
    }

    public void save(ApplicationDTO applicationDTO)
    {
        applicationRepository.save(mapper.map(applicationDTO, Application.class));
    }

    public List<ApplicationDTO> fetchByUserId(Integer userId) throws EntityNotFound {
        List<Application>applications=applicationRepository.fetchByUserId(userId);
        if (ObjectUtil.isNullOrEmpty(applications))
        {
            throw new EntityNotFound("application.not.exists");
        }
        return applications.stream().map(entity->mapper.map(entity, ApplicationDTO.class)).toList();
    }

}
