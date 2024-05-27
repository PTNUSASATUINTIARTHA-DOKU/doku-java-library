package com.doku.sdk.dokujavalibrary.validation.annotation;

import com.doku.sdk.dokujavalibrary.validation.custom.DateIso8601Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = DateIso8601Validator.class)
public @interface DateIso8601 {

    String message() default "Date must using ISO-8601 format (yyyy-MM-dd'T'HH:mm:ssXXX) AND dateIn = {dateIn} AND allowBlank = {allowBlank}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    DateIn dateIn() default DateIn.ANY_TIME;
    boolean allowBlank() default true;

    enum DateIn {
        ANY_TIME, PAST, FUTURE
    }
}
