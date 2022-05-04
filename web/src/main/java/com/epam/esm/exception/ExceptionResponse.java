package com.epam.esm.exception;

public class ExceptionResponse {

    private final String errorMessage;
    private final int errorCode;


    public ExceptionResponse(String errorMessage, Integer errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
