package com.doku.sdk.dokujavalibrary.validation.group;

import com.doku.sdk.dokujavalibrary.validation.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.PatternValidation;
import com.doku.sdk.dokujavalibrary.validation.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.SizeValidation;

import javax.validation.GroupSequence;

@GroupSequence({MandatoryValidation.class, SafeStringValidation.class, PatternValidation.class, SizeValidation.class})
public interface GeneralSequenceValidation {
}
