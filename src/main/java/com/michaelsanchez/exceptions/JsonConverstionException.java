package com.michaelsanchez.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonConverstionException extends Throwable {
    public JsonConverstionException(JsonProcessingException e) {
        super(e);
    }
}
