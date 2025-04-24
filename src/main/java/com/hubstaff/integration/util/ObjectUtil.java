package com.hubstaff.integration.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ObjectUtil {

    // Validate that the DTO is not null and return true if valid, false otherwise
    public static boolean validateDto(Object dto) {
        return Objects.nonNull(dto);  // Return true if not null, otherwise false
    }

    // Check if an object is null or empty based on its type (String, Collection, Map)
    public static boolean isNullOrEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return ((String) object).trim().isEmpty();
        }
        if (object instanceof Collection<?>) {
            return ((Collection<?>) object).isEmpty();
        }
        if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).isEmpty();
        }
        return false;
    }
}
