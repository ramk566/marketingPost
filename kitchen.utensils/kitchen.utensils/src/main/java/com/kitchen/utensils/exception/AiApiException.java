package com.kitchen.utensils.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiApiException extends Exception {
    private final int statusCode;

    public AiApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}

