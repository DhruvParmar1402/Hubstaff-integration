package com.hubstaff.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @JsonProperty("users")
    List<UserDTO> users;

}
