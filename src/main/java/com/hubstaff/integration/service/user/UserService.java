package com.hubstaff.integration.service.user;

import com.hubstaff.integration.dto.UserDTO;
import com.hubstaff.integration.exception.EntityNotFound;

import java.util.List;
import java.util.Map;

public interface UserService {
    void fetchAndSaveUsers() throws EntityNotFound;
    List<UserDTO> getUsers(Integer organizationId);
    List<UserDTO> getNewUsers(Long organizationId);
}
