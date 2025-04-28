package com.hubstaff.integration.service.application;

import com.hubstaff.integration.dto.ApplicationDTO;
import com.hubstaff.integration.exception.EntityNotFound;

import java.util.List;

public interface ApplicationService {
    void save(ApplicationDTO applicationDTO);
    List<ApplicationDTO> fetchByUserId(Integer userId) throws EntityNotFound;
}
