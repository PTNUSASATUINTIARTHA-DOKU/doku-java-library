package com.doku.sdk.dokujavalibrary.validation.group;

import javax.validation.GroupSequence;

@GroupSequence({MandatoryValidation.class,
        SafeStringValidation.class,
        PatternValidation.class,
        SizeValidation.class,
        AmountValidation.class,
        LengthValidation.class})
public interface GeneralSequenceValidation {
}
