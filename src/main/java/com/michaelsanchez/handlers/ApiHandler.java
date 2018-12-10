package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.michaelsanchez.controllers.Controller;
import com.michaelsanchez.exceptions.JsonConverstionException;
import com.michaelsanchez.utils.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiHandler extends AbstractHandler {
    private Injector injector;
    private ObjectWriter objectWriter;

    @Inject
    public ApiHandler(ObjectWriter objectWriter) {
        this.objectWriter = objectWriter;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String controllerBaseName = target.replaceFirst("/api/", "");

        try {
            String packageName = Controller.class.getPackage().getName();
            String className = StringUtils.ucFirst(controllerBaseName + "Controller");
            Class<?> controllerClass = Class.forName(packageName + "." + className);
            Controller controller = (Controller) injector.getInstance(controllerClass);
            Object o = controller.handleRequest(request);
            handleResponse(response, o);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            baseRequest.setHandled(true);
        }
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    private void handleResponse(HttpServletResponse response, Object o) throws IOException {
        try {
            //TODO Check for nulls, Respond with BADREQUEST if null
            // Loging frameworks to use and why and where
            response.getWriter().println(getJsonConversion(o));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable e) {
            response.getWriter().println(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String getJsonConversion(Object o) throws JsonConverstionException {
        try {
            return objectWriter.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new JsonConverstionException(e);
        }
    }

}


/**
 * Guice -> make a provider that is going to provide an object mapper to the api handler class
 * Can we get the Object writer instead ?
 * <p>
 * Is this an Uber thing or why? -> dependency injection when there is no dependency
 * How to compose Guice modules(Parent dependencies and shit like that)
 */