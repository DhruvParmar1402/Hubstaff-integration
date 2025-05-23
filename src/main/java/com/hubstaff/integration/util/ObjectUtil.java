package com.hubstaff.integration.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ObjectUtil {


    public static boolean validateDto(Object dto) {
        return Objects.nonNull(dto);
    }

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
