package ru.javawebinar.topjava.graduation.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;

public class DateTimeUtil {
    public static final int RESTRICTION_HOUR = 11;
    // Time restriction should be tied to a certain timezone, i.e. ZoneId.of("Europe/Moscow"),
    // System default zone (ZoneId.systemDefault()) must be replaced with a desired timezone
    public static final ZoneId TIMEZONE = ZoneId.systemDefault();
    // DataBase doesn't support LocalDate.MIN/MAX
    private static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    private DateTimeUtil() {
    }

    public static LocalDate adjustStartDate(LocalDate localDate) {
        return adjustDate(localDate, MIN_DATE);
    }

    public static LocalDate adjustEndDate(LocalDate localDate) {
        return adjustDate(localDate, MAX_DATE);
    }

    private static LocalDate adjustDate(LocalDate localDate, LocalDate defaultDate) {
        return localDate != null ? localDate : defaultDate;
    }

    public static LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static LocalDate getZoneAwareCurrentDate() {
        return LocalDate.now(TIMEZONE);
    }
}