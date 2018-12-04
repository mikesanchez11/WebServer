package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.michaelsanchez.exceptions.JsonConverstionException;
import com.michaelsanchez.models.ImageModel;
import com.michaelsanchez.models.ImageResultModel;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

public class ApiHandler extends AbstractHandler {
    private ObjectWriter mObjectWriter;

    @Inject
    public ApiHandler(ObjectWriter objectWriter) {
        mObjectWriter = objectWriter;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ImageResultModel imageResultModel = new ImageResultModel();
            imageResultModel.addImage(new ImageModel("id", "title", URI.create("yahoo.com")));
            response.getWriter().println(getJsonConversion(imageResultModel));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonConverstionException e) {
            response.getWriter().println(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            baseRequest.setHandled(true);
        }
    }

    private String getJsonConversion(ImageResultModel imageResultModel) throws JsonConverstionException {
        try {
            return mObjectWriter.writeValueAsString(imageResultModel);
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