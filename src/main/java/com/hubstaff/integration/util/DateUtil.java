package com.hubstaff.integration.util;

import java.time.*;

public class DateUtil {
    public static Instant startOfPreviousDay()
    {
        ZoneId zone = ZoneOffset.UTC;
        LocalDate yesterday = LocalDate.now(zone).minusDays(1);
        Instant startOfDay = yesterday.atStartOfDay(zone).toInstant();
        return startOfDay;
    }

    public static Instant endOfPreviousDay()
    {
        ZoneId zone = ZoneOffset.UTC;
        LocalDate yesterday = LocalDate.now(zone).minusDays(1);
        Instant endOfDay = yesterday.atTime(23, 59, 59).atZone(zone).toInstant();
        return endOfDay;
    }

    public static Instant startOfPreviousMonth()
    {
        ZoneId zone=ZoneOffset.UTC;
        LocalDate lastMonth=LocalDate.now(zone).minusMonths(1);
        lastMonth=LocalDate.of(lastMonth.getYear(),lastMonth.getMonthValue(),1);
        Instant startOfDay=lastMonth.atStartOfDay(zone).toInstant();
        return startOfDay;
    }

    public static Instant endOfPreviousMonth()
    {
        ZoneId zone = ZoneOffset.UTC;
        LocalDate yesterday = LocalDate.now(zone).minusMonths(1);
        int days=YearMonth.of(yesterday.getYear(), yesterday.getMonthValue()).lengthOfMonth();
        yesterday=LocalDate.of(yesterday.getYear(),yesterday.getMonthValue(),days);
        Instant endOfDay = yesterday.atTime(23, 59, 59).atZone(zone).toInstant();
        return endOfDay;
    }

    public static Instant startOfCurrentMonth()
    {
        ZoneId zone=ZoneOffset.UTC;
        LocalDate thisMonth=LocalDate.now(zone).minusMonths(0);
        thisMonth=LocalDate.of(thisMonth.getYear(),thisMonth.getMonthValue(),1);
        Instant startOfDay=thisMonth.atStartOfDay().atZone(zone).toInstant();
        return startOfDay;
    }

    public static Instant endOfCurrentMonth()
    {
        ZoneId zone = ZoneOffset.UTC;
        LocalDate yesterday = LocalDate.now(zone).minusMonths(0);
        int days=YearMonth.of(yesterday.getYear(), yesterday.getMonthValue()).lengthOfMonth();
        yesterday=LocalDate.of(yesterday.getYear(),yesterday.getMonthValue(),days);
        Instant endOfDay = yesterday.atTime(23, 59, 59).atZone(zone).toInstant();
        return endOfDay;
    }

    public static ZoneId getZoneId()
    {
        return ZoneOffset.UTC;
    }
}
