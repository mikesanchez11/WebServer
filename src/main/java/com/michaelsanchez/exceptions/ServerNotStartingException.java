package com.michaelsanchez.exceptions;

public class ServerNotStartingException extends Throwable {
    public ServerNotStartingException(Exception e) {
        super(e);
    }
}
