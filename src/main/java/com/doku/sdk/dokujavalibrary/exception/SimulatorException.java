package com.doku.sdk.dokujavalibrary.exception;

import lombok.Getter;

@Getter
public class SimulatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String responseCode;
    private final Object object;

    public SimulatorException(String responseCode, String responseMessage, Object object) {
        super(responseMessage);
        this.responseCode = responseCode;
        this.object = object;
    }
}
