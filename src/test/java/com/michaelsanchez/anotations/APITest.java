package com.michaelsanchez.anotations;

import com.michaelsanchez.controllers.FlickrController;
import org.junit.Assert;
import org.junit.Test;

public class APITest {
    @Test
    public void ddtestAnnotationHasValue() {
        Assert.assertEquals("999", "999");
    }


    @Test
    public void testAnnotationHasValue() {
        Class<? extends FlickrController> flickrControllerClass = FlickrController.class;

        // get its API annotation
        API annotation = flickrControllerClass.getAnnotation(API.class);
        String valueFromAnnotation = annotation.value();
        Assert.assertEquals("/api/flickr", valueFromAnnotation);
    }
}
