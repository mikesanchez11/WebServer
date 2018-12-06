package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.michaelsanchez.FlickrClient;
import com.michaelsanchez.exceptions.JsonConverstionException;
import com.michaelsanchez.models.FlickrResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiHandler extends AbstractHandler {
    private ObjectWriter mObjectWriter;

    @Inject
    public ApiHandler(ObjectWriter objectWriter) {
        mObjectWriter = objectWriter;
    }

    @Inject
    private FlickrClient flickrClient;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //TODO Check for nulls, Respond with BADREQUEST if null
            // Loging frameworks to use and why and where
            FlickrResponse flickrResponse = flickrClient.findImagesByKeyword(request.getParameter("q"));
            response.getWriter().println(getJsonConversion(flickrResponse));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable e) {
            response.getWriter().println(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            baseRequest.setHandled(true);
        }
    }

    private String getJsonConversion(Object o) throws JsonConverstionException {
        try {
            return mObjectWriter.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new JsonConverstionException(e);
        }
    }
}


/**
    Guice -> make a provider that is going to provide an object mapper to the api handler class
    Can we get the Object writer instead ?

    Is this an Uber thing or why? -> dependency injection when there is no dependency
    How to compose Guice modules(Parent dependencies and shit like that)
 */