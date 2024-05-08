package com.doku.sdk.dokujavalibrary.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DateUtils {

    public String getISO8601StringFromDateUTC(LocalDateTime date, DateTimeFormatter df) {
        if (date != null) {
            ZonedDateTime ldtZoned = date.atZone(ZoneId.systemDefault());
            ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));
            return df.format(utcZoned);
        } else {
            return null;
        }
    }
}
