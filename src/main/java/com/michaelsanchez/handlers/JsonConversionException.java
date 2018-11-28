package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonConversionException extends Exception {
    public JsonConversionException(Throwable cause) {
        super(cause);
    }
}
