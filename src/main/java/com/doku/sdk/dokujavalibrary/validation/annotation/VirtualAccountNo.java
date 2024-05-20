package com.doku.sdk.dokujavalibrary.validation.annotation;

import com.doku.sdk.dokujavalibrary.validation.custom.VirtualAccountNoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VirtualAccountNoValidator.class)
public @interface VirtualAccountNo {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
