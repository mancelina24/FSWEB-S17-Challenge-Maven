package com.workintech.spring17challenge.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCourseCreditException extends ApiException {
    public InvalidCourseCreditException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
