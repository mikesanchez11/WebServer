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

        try {
            String fullnameWithPackage = getControllerFullyQualifiedClassName(target);
            Controller controller = getController(fullnameWithPackage);
            Object o = controller.handleRequest(request);
            handleResponse(response, o);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            baseRequest.setHandled(true);
        }
    }

    private Controller getController(String fullnameWithPackage) throws ClassNotFoundException {
        Class<?> controllerClass = Class.forName(fullnameWithPackage);
        return (Controller) injector.getInstance(controllerClass);
    }

    /**
     * Based on the path in bthe URL we use a naming convention to determine the name of the controller class.
     *  /api/flickr -> uppercaseFirst(flickr) + "Controller"
     *  package name is dervived by hoping Controller.class isa in thew same package as all the other shit
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
                    System.out.println("value: " + apiAnnotation.value());
                }
            }
        }

        return classMap.get(target).getCanonicalName();
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

// Accessing annotation
// How do i get to these annotations