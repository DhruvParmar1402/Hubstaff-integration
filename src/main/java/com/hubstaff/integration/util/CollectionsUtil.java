package com.hubstaff.integration.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;

public class CollectionsUtil<T> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final ZoneId zone = ZoneId.systemDefault();

    public List<T> filterFromPreviousDay(List<T> list, Function<T, String> createdAtExtractor) {
        LocalDate yesterday = LocalDate.now(zone).minusDays(1);
        Instant startOfDay = yesterday.atStartOfDay(zone).toInstant();
        Instant endOfDay = yesterday.atTime(23, 59, 59).atZone(zone).toInstant();

        return list.stream()
                .filter(item -> {
                    try {
                        String createdAtStr = createdAtExtractor.apply(item);
                        if (createdAtStr == null) return false;

                        Instant createdAt = LocalDateTime.parse(createdAtStr, formatter)
                                .atZone(zone)
                                .toInstant();
                        return !createdAt.isBefore(startOfDay) && !createdAt.isAfter(endOfDay);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
