package com.doku.sdk.dokujavalibrary.validation.group;

import com.doku.sdk.dokujavalibrary.validation.SafeStringValidation;

import javax.validation.GroupSequence;

@GroupSequence({SafeStringValidation.class})
public interface GeneralSequenceValidation {
}
