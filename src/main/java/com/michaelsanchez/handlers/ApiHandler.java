package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.michaelsanchez.controllers.Controller;
import com.michaelsanchez.exceptions.JsonConverstionException;
import com.michaelsanchez.utils.HttpWrapper;
import com.michaelsanchez.utils.PrintWriterWrapper;
import org.eclipse.jetty.server.HttpWriter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ApiHandler extends AbstractHandler {
    private Injector injector;
    private ObjectWriter objectWriter;
    private String packageName;

    private static Logger LOGGER = LoggerFactory.getLogger(ApiHandler.class);

    @Inject
    public ApiHandler(ObjectWriter objectWriter, String packageName) {
        this.objectWriter = objectWriter;
        this.packageName = packageName;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        Controller controller;
        HttpWrapper httpWrapper = new HttpWrapper(baseRequest, response);
        LOGGER.debug("About to handle {}", target);

        try {
            controller = getController(target);
        } catch (Throwable e) {
            LOGGER.warn("Controller was not found for {}", target);
            httpWrapper.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // WE grabbed the controller above
        // We used the controller below

        try {
            Object o = controller.handleRequest(request);
            writeResponse(new PrintWriterWrapper(response.getWriter()), o);
            httpWrapper.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable throwable) {
            LOGGER.error("Error handling {}", target, throwable);

            httpWrapper.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    Controller getController(String target) throws ClassNotFoundException {
        String fullnameWithPackage = getControllerFullyQualifiedClassName(target);

        if (fullnameWithPackage == null) {
            throw new ClassNotFoundException();
        }

        LOGGER.trace("Grabbing controller for {}", fullnameWithPackage);
        Class<?> controllerClass = Class.forName(fullnameWithPackage);
        return (Controller) injector.getInstance(controllerClass);
    }

    /**
     * Based on the path in bthe URL we use a naming convention to determine the name of the controller class.
     * /api/flickr -> uppercaseFirst(flickr) + "Controller"
     * package name is dervived by hoping Controller.class isa in thew same package as all the other shit
     *
     * @param target
     * @return
     * @throws ClassNotFoundException
     */
    String getControllerFullyQualifiedClassName(String target) {
        LOGGER.debug("Grabbing full class name for {}", target);

        ControllerScanner controllerScanner = new ControllerScanner(packageName);
        controllerScanner.scan();
        Class controllerClass = controllerScanner.getControllerClass(target);

        if (controllerClass != null) {
            String fullClassName = controllerClass.getCanonicalName();
            LOGGER.debug("Using {} for {}", fullClassName, target);
            return fullClassName ;
        } else {
            LOGGER.warn("Full name was null for {}", target);
            return null;
        }
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    void writeResponse(PrintWriterWrapper writerWrapper, Object o) {
        try {
            writerWrapper.println(getJsonConversion(o));
        } catch (Throwable e) {
            writerWrapper.println(e.getLocalizedMessage());
        }
    }

    String getJsonConversion(Object o) throws JsonConverstionException {
        try {
            return objectWriter.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new JsonConverstionException(e);
        }
    }

    public Injector getInjector() {
        return injector;
    }

    public void setPackageName(String name) {
        this.packageName = name;
    }
}