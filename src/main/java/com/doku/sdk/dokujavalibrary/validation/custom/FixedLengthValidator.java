package com.doku.sdk.dokujavalibrary.validation.custom;

import com.doku.sdk.dokujavalibrary.validation.annotation.FixedLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FixedLengthValidator implements ConstraintValidator<FixedLength, String> {

    private List<Integer> length;

    @Override
    public void initialize(FixedLength constraintAnnotation) {
        this.length = Arrays.stream(constraintAnnotation.length()).boxed().collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || this.length.contains(value.length());
    }
}
