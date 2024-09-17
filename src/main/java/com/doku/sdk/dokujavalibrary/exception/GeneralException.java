package com.doku.sdk.dokujavalibrary.exception;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String responseCode;

    public GeneralException(String responseCode, String responseMessage) {
        super(responseMessage);
        this.responseCode = responseCode;
    }
}
