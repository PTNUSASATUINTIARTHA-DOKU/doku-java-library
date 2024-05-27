package com.doku.sdk.dokujavalibrary.validation.custom;

import com.doku.sdk.dokujavalibrary.validation.annotation.DateIso8601;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Component
@Slf4j
public class DateIso8601Validator implements ConstraintValidator<DateIso8601, String> {

    public static final String FORMAT_DATE_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private DateIso8601 dateIso8601;

    @Override
    public void initialize(DateIso8601 dateIso8601)
    {
        this.dateIso8601 = dateIso8601;
    }

    @Override
    public boolean isValid(String iso8601DateAsString, ConstraintValidatorContext context) {
        if (iso8601DateAsString == null) {
            return true;
        }

        if (iso8601DateAsString.isBlank()) {
            return dateIso8601.allowBlank();
        }

        boolean isValid = false;

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIMESTAMP);
            TemporalAccessor temporalAccessor = dateTimeFormatter.parse(iso8601DateAsString);

            if (dateIso8601.dateIn() == DateIso8601.DateIn.FUTURE) {
                isValid = Instant.from(temporalAccessor).isAfter(Instant.now());
            } else if(dateIso8601.dateIn() == DateIso8601.DateIn.PAST) {
                isValid = Instant.from(temporalAccessor).isBefore(Instant.now());
            } else {
                isValid = true;
            }
        } catch(Exception e) {
            log.warn(e.getMessage());
        }

        return isValid;
    }
}
