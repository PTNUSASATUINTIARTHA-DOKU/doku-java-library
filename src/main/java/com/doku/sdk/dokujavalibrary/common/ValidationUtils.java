package com.doku.sdk.dokujavalibrary.common;

import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import com.doku.sdk.dokujavalibrary.validation.group.GeneralSequenceValidation;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;

public class ValidationUtils {

    private ValidationUtils() {
    }

    private static final Validator javaxValidator = buildDefaultValidatorFactory().getValidator();
    private static final SpringValidatorAdapter validator = new SpringValidatorAdapter(javaxValidator);

    public static void validateRequest(Object object) {
        Errors errors = new BeanPropertyBindingResult(object, object.getClass().getName());
        validator.validate(object, errors, GeneralSequenceValidation.class);

        var fieldError = errors.getFieldError();
        if (fieldError != null) {
            throw new BadRequestException(fieldError.getCode(), fieldError.getDefaultMessage());
        }
    }
}
