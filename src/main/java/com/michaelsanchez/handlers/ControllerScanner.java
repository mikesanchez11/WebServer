package com.michaelsanchez.handlers;

import com.michaelsanchez.anotations.API;
import com.michaelsanchez.controllers.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private String packageName;
    private Map<String, Class> classMap = new HashMap<>();
    private static Logger LOGGER = LoggerFactory.getLogger(ControllerScanner.class);

    public ControllerScanner(String packageName) {
        this.packageName = packageName;
    }

    public void scan() {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Controller>> allClasses =
                reflections.getSubTypesOf(Controller.class);

        LOGGER.trace("Scanning {} classes", allClasses.size());

        for (Class clazz : allClasses) {
            Annotation[] annotations = clazz.getAnnotations();

            LOGGER.trace("Got {} annotations from class {}", annotations.length, clazz);

            for (Annotation annotation : annotations) {
                if (annotation instanceof API) {
                    API apiAnnotation = (API) annotation;
                    classMap.put(apiAnnotation.value(), clazz);
                    LOGGER.trace("Found {} for {}. Skipping the rest", annotation, clazz);
                    break;
                }
            }
        }
    }

    public Class getControllerClass(String target) {
        return classMap.get(target);
    }
}
