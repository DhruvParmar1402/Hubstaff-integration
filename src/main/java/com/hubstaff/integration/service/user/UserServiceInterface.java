package com.hubstaff.integration.service.user;

import com.hubstaff.integration.dto.UserDTO;

import java.util.List;

public interface UserServiceInterface {
    void fetchAndSaveUsers();
    List<UserDTO> getUsers(String organizationName);
}
