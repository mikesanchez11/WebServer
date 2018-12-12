package com.michaelsanchez.controllers;

import com.google.inject.Inject;
import com.michaelsanchez.FlickrClient;
import com.michaelsanchez.anotations.API;
import com.michaelsanchez.exceptions.FlickrClientException;
import com.michaelsanchez.models.FlickrResponse;

import javax.servlet.http.HttpServletRequest;

@API("/api/flickr")
@Deprecated
public class FlickrController implements Controller {

    public static final String QUERY = "q";
    private FlickrClient flickrClient;

    @Inject
    public FlickrController(FlickrClient flickrClient) {
        this.flickrClient = flickrClient;
    }

    @Override
    public FlickrResponse handleRequest(HttpServletRequest request) throws FlickrClientException {
        return flickrClient.findImagesByKeyword(request.getParameter(QUERY));
    }
}
