package com.hubstaff.integration.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class CollectionsUtil<T> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public List<T> filterFromPreviousDay(List<T> list, Function<T, String> createdAtExtractor) {

        Instant startOfDay=DateUtil.startOfPreviousDay();
        Instant endOfDay=DateUtil.endOfPreviousDay();
        ZoneId zone=DateUtil.getZoneId();
        return list.stream()
                .filter(item -> {
                    try {
                        String createdAtStr = createdAtExtractor.apply(item);
                        if (createdAtStr == null) return false;

                        String trimmedTimed=createdAtStr.substring(0,19);

                        Instant createdAt = LocalDateTime.parse(trimmedTimed, formatter)
                                .atZone(zone)
                                .toInstant();

                        return !createdAt.isBefore(startOfDay) && !createdAt.isAfter(endOfDay);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();
    }
}