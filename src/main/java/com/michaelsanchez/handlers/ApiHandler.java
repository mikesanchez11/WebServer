package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.michaelsanchez.anotations.API;
import com.michaelsanchez.controllers.Controller;
import com.michaelsanchez.exceptions.JsonConverstionException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApiHandler extends AbstractHandler {
    private Injector injector;
    private ObjectWriter objectWriter;

    @Inject
    public ApiHandler(ObjectWriter objectWriter) {
        this.objectWriter = objectWriter;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        Controller controller;

        try {
            String fullnameWithPackage = getControllerFullyQualifiedClassName(target);
            controller = getController(fullnameWithPackage);
        } catch (Throwable e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } finally {
            baseRequest.setHandled(true);
        }

        try {
            Object o = controller.handleRequest(request);
            writeResponse(response, o);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable throwable) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            baseRequest.setHandled(true);
        }
    }

    private Controller getController(String fullnameWithPackage) throws ClassNotFoundException {
        if (fullnameWithPackage == null) {
            throw new ClassNotFoundException();
            // TODO: Log something
        }

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
    private String getControllerFullyQualifiedClassName(String target) {
        Map<String, Class> classMap = new HashMap<>();

        Reflections reflections = new Reflections("com.michaelsanchez.controllers");
        Set<Class<? extends Controller>> allClasses =
                reflections.getSubTypesOf(Controller.class);

        for (Class clazz : allClasses) {
            Annotation[] annotations = clazz.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation instanceof API) {
                    API apiAnnotation = (API) annotation;
                    classMap.put(apiAnnotation.value(), clazz);
                    // TODO: LOG THIS
                    break;
                }
            }
        }

        return classMap.containsKey(target) ? classMap.get(target).getCanonicalName() : null;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    private void writeResponse(HttpServletResponse response, Object o) throws IOException {
        try {
            response.getWriter().println(getJsonConversion(o));
        } catch (Throwable e) {
            response.getWriter().println(e.getLocalizedMessage());
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