package com.hubstaff.integration.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Component
@RequestScope
public class TokenProvider {
    private String token;

    public void extractToken(HttpServletRequest request) throws IllegalAccessException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token=header.substring(7);
        }
        throw new IllegalAccessException("Authorization token missing or invalid.");
    }
}
