package com.michaelsanchez.handlers;

import com.michaelsanchez.dummycontroller.Dummy1;
import com.michaelsanchez.dummycontroller.Dummy2;
import com.michaelsanchez.dummycontroller.children.Child1;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerScannerTest {

    private static ControllerScanner controllerScanner;

    @BeforeClass
    public static void instantiateControllerScanner() {
        controllerScanner = new ControllerScanner(Dummy1.class.getPackage().getName());
        controllerScanner.scan();
    }

    @Test
    public void controllerScanner_scanValidApi() {
        assertNotNull(controllerScanner);

        assertEquals(Dummy1.class, controllerScanner.getControllerClass("/api/dummy1"));
        assertEquals(Dummy2.class, controllerScanner.getControllerClass("/api/dummy2"));
    }

    @Test
    public void controllerScanner_scanNotValidAPI() {
        assertNull(controllerScanner.getControllerClass("/api/dummy3"));
    }

    @Test
    public void controllerScanner_scanTestChildPackage() {
        assertEquals(Child1.class, controllerScanner.getControllerClass("/api/child"));
    }

    @Test
    public void controllerScanner_scanWithInvalidPackageNameAndValidAPI() {
        ControllerScanner scanner = new ControllerScanner("invalid/$^&*");
        scanner.scan();
        assertNull(scanner.getControllerClass("/api/dummy1"));
    }

    @Test
    public void controllerScanner_scanWithInvalidPackageNameAndInvalidAPI() {
        ControllerScanner scanner = new ControllerScanner("invalid/$^&*");
        scanner.scan();
        assertNull(scanner.getControllerClass("/api/not/valid"));
    }

    @Test
    public void controllerScanner_scanWithInvalidPackageNameAndNullAPI() {
        ControllerScanner scanner = new ControllerScanner("invalid/$^&*");
        scanner.scan();
        assertNull(scanner.getControllerClass(null));
    }
}