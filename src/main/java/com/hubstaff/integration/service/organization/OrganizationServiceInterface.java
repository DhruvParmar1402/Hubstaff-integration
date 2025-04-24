package com.hubstaff.integration.service.organization;

import com.hubstaff.integration.dto.OrganizationDTO;

import java.util.List;

public interface OrganizationServiceInterface {
    void fetchAndSave();
    List<OrganizationDTO> getOrganizations();
}
