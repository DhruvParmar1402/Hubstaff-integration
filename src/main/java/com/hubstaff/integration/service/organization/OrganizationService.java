package com.hubstaff.integration.service.organization;

import com.hubstaff.integration.dto.OrganizationDTO;
import com.hubstaff.integration.exception.EntityNotFound;

import java.util.List;

public interface OrganizationService {
    void fetchAndSave() throws EntityNotFound;
    List<OrganizationDTO> getOrganizations();
}
