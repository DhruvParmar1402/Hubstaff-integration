package com.hubstaff.integration.service.application;

import com.hubstaff.integration.dto.ApplicationDTO;
import com.hubstaff.integration.entity.ApplicationEntity;
import com.hubstaff.integration.exception.EntityNotFound;
import com.hubstaff.integration.repository.ApplicationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationServiceInterface{

    private final ApplicationRepository applicationRepository;
    private final ModelMapper mapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ModelMapper modelMapper)
    {
        this.applicationRepository=applicationRepository;
        this.mapper=modelMapper;
    }

    public void save(ApplicationDTO applicationDTO)
    {
        applicationRepository.save(mapper.map(applicationDTO, ApplicationEntity.class));
    }

    public List<ApplicationDTO> fetchByUserId(Integer userId) throws EntityNotFound {
        List<ApplicationEntity>applications=applicationRepository.fetchByUserId(userId);
        if(applications==null || applications.isEmpty())
        {
            throw new EntityNotFound("userApp.notFound");
        }
        return applications.stream().map(entity->mapper.map(entity, ApplicationDTO.class)).toList();
    }

}
