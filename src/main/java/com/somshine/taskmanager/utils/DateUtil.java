package com.somshine.taskmanager.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public class DateUtil {

    public static LocalDateTime getLocalDateTimeFromString(String dateStr) throws ParseException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            LocalDateTime localDate = LocalDate.parse(dateStr, formatter).atStartOfDay();
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
//            return localDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
