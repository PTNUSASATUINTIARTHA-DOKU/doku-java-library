package com.doku.sdk.dokujavalibrary.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String responseCode;

    public BadRequestException(String responseCode, String responseMessage) {
        super(responseMessage);
        this.responseCode = responseCode;
    }
}
