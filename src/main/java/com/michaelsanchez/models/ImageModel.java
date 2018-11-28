package com.michaelsanchez.models;

import java.net.URI;
import java.net.URL;

public class ImageModel {

    private String id;
    private String title;
    private URI uri;

    public ImageModel() {
    }

    public ImageModel(String id, String title, URI uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
