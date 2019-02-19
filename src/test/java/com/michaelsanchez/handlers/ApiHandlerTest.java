package com.michaelsanchez.handlers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.michaelsanchez.ApiHandlerModule;
import com.michaelsanchez.FlickrClientModule;
import com.michaelsanchez.anotations.API;
import com.michaelsanchez.controllers.Controller;
import com.michaelsanchez.dummycontroller.Dummy1;
import com.michaelsanchez.exceptions.JsonConverstionException;
import com.michaelsanchez.utils.PrintWriterWrapper;
import org.eclipse.jetty.server.Request;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.junit.Assert.*;

public class ApiHandlerTest {

    @Test
    public void getControllerFullyQualifiedClassName_invalidTargets() {
        String[] invalidTargets = new String[] {
          " ", "\t", null, "aosuhdoweud0238"
        };

        ApiHandler apiHandler = new ApiHandler(null, Dummy1.class.getPackage().getName());

        for (String invalidTarget : invalidTargets) {
            String className = apiHandler.getControllerFullyQualifiedClassName(invalidTarget);
            assertNull(className);
        }
    }

    @Test
    public void getControllerFullyQualifiedClassName_validTargets() {
        API annotation = Dummy1.class.getAnnotation(API.class);

        ApiHandler apiHandler = new ApiHandler(null, Dummy1.class.getPackage().getName());

        String className = apiHandler.getControllerFullyQualifiedClassName(annotation.value());
        assertEquals(Dummy1.class.getCanonicalName(), className);
    }

    @Test
    public void setInjector_setting() {
        ApiHandler apiHandler = new ApiHandler(null, null);
        Injector injector = Guice.createInjector(new ApiHandlerModule(), new FlickrClientModule());

        apiHandler.setInjector(injector);
        assertEquals(injector, apiHandler.getInjector());
    }

    @Test
    public void getController_validTarget() throws ClassNotFoundException {
        API annotation = Dummy1.class.getAnnotation(API.class);
        ApiHandler apiHandler = getApiHandler();
        Controller controller = apiHandler.getController(annotation.value());
        assertTrue(controller instanceof Dummy1);
    }

    @Test
    public void getController_invalidTarget() {
        ApiHandler apiHandler = getApiHandler();
        try {
            Controller controller = apiHandler.getController("random");
            assertTrue(false);
        } catch (ClassNotFoundException e) {
            assertTrue(true);
        }
    }

    private ApiHandler getApiHandler() {
        Injector injector = Guice.createInjector(new ApiHandlerModule(), new FlickrClientModule());


        ApiHandler apiHandler = injector.getInstance(ApiHandler.class);
        apiHandler.setPackageName(Dummy1.class.getPackage().getName());
        apiHandler.setInjector(injector);
        return apiHandler;
    }

    @Test
    public void getJsonConversion_valid() {
        ApiHandler apiHandler = getApiHandler();

        try {
            apiHandler.getJsonConversion(new int[]{1,2,3});
            assertTrue(true);
        } catch (JsonConverstionException e) {
            assertTrue(false);
        }
    }

    @Test
    public void getJsonConversion_invalid() {
        ApiHandler apiHandler = getApiHandler();

        try {
            apiHandler.getJsonConversion(new ClassThatJacksonCannotSerialize());
            assertTrue(false);
        } catch (JsonConverstionException e) {
            assertTrue(true);
        }
    }

    @Test
    public void writeResponse_valid() {
        ApiHandler apiHandler = getApiHandler();
        PrintWriterWrapper writerWrapper = new PrintWriterWrapper(new PrintWriter(new ByteArrayOutputStream()));

        String o = "Hello World";
        apiHandler.writeResponse(writerWrapper, o);

        assertEquals("\"" + o + "\"", writerWrapper.getBufferContent());
    }

    @Test
    public void writeResponse_failedCase() {
        ApiHandler apiHandler = getApiHandler();
        PrintWriterWrapper writerWrapper = new PrintWriterWrapper(new PrintWriter(new ByteArrayOutputStream()));

        String o = "Hello World";
        apiHandler.writeResponse(writerWrapper, new ClassThatJacksonCannotSerialize());

        assertTrue(writerWrapper.getBufferContent().contains("No serializer found for class"));
    }

    @Test
    public void handle_invalidTarget() {
        ApiHandler apiHandler = getApiHandler();

        apiHandler.handle();

    }

    private static class ClassThatJacksonCannotSerialize {
        private final ClassThatJacksonCannotSerialize self = this;

        @Override
        public String toString() {
            return self.getClass().getName();
        }
    }
}