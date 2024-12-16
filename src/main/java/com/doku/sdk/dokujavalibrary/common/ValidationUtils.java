package com.doku.sdk.dokujavalibrary.common;

import com.doku.sdk.dokujavalibrary.enums.SnapResponseEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.doku.sdk.dokujavalibrary.validation.group.GeneralSequenceValidation;
import org.springframework.http.HttpStatus;
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

    public static void validateRequest(Object object, String serviceCode) {
        Errors errors = new BeanPropertyBindingResult(object, object.getClass().getName());
        validator.validate(object, errors, GeneralSequenceValidation.class);

        var fieldError = errors.getFieldError();
        if (null != fieldError) {
            if ("NotBlank".equals(fieldError.getCode()) ||
                    "NotNull".equals(fieldError.getCode()) ||
                    "NotEmpty".equals(fieldError.getCode())) {
                throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR.value() + serviceCode + SnapResponseEnum.SUCCESSFUL.getResponseCode(),
                        SnapResponseEnum.INVALID_MANDATORY_FIELD.getResponseMessage() + fieldError.getField());
            } else if ("Size".equals(fieldError.getCode()) ||
                    "Pattern".equals(fieldError.getCode()) ||
                    "Length".equals(fieldError.getCode()) ||
                    "FixedLength".equals(fieldError.getCode()) ||
                    "Email".equals(fieldError.getCode()) ||
                    "DateIso8601".equals(fieldError.getCode())) {
                throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR.value() + serviceCode + SnapResponseEnum.SUCCESSFUL.getResponseCode(),
                        SnapResponseEnum.INVALID_FIELD_FORMAT.getResponseMessage() + fieldError.getField());
            } else {
                throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR.value() + serviceCode + SnapResponseEnum.GENERAL_ERROR.getResponseCode(),
                        SnapResponseEnum.INVALID_FIELD_FORMAT.getResponseMessage() + fieldError.getField());
            }
        }
    }
}
