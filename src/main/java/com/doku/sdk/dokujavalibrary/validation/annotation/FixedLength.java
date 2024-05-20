package com.doku.sdk.dokujavalibrary.validation.annotation;

import com.doku.sdk.dokujavalibrary.validation.custom.FixedLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FixedLengthValidator.class})
public @interface FixedLength {

    int[] length();
    String message() default "Invalid Length";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
