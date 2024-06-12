package com.doku.sdk.dokujavalibrary.validation.annotation;

import com.doku.sdk.dokujavalibrary.validation.custom.SafeStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SafeStringValidator.class})
public @interface SafeString {
    String message() default "Invalid Safe String Value";

    String regexp() default "^[a-zA-Z0-9.\\-/+,=_:'@% ]*$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
