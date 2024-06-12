package com.doku.sdk.dokujavalibrary.validation.custom;

import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class SafeStringValidator implements ConstraintValidator<SafeString, String> {

    private static final Logger log = LoggerFactory.getLogger(SafeStringValidator.class);
    private SafeString safeString;

    public SafeStringValidator() {
    }

    public void initialize(SafeString safeString) {
        this.safeString = safeString;
    }

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String regex = this.safeString.regexp();

        log.debug("value : {}", value);
        log.debug("regex : {}", regex);
        return Objects.toString(value, "").matches(regex);
    }
}
