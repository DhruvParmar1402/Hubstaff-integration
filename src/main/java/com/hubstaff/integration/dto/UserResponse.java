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

    @JsonProperty("pagination.next_page_start_id")
    private Long page;

}
