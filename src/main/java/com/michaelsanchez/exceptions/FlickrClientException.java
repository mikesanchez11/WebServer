package com.michaelsanchez.exceptions;

import java.io.IOException;

public class FlickrClientException extends Throwable {
    public FlickrClientException(IOException e) {
        super(e);
    }
}
