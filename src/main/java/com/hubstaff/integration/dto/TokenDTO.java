package com.hubstaff.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
