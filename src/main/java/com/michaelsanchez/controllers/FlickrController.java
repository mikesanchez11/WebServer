package com.michaelsanchez.controllers;

import com.google.inject.Inject;
import com.michaelsanchez.FlickrClient;
import com.michaelsanchez.exceptions.FlickrClientException;
import com.michaelsanchez.models.FlickrResponse;

import javax.servlet.http.HttpServletRequest;

public class FlickrController implements Controller {

    private FlickrClient flickrClient;

    @Inject
    public FlickrController(FlickrClient flickrClient) {
        this.flickrClient = flickrClient;
    }

    @Override
    public FlickrResponse handleRequest(HttpServletRequest request) throws FlickrClientException {
        return flickrClient.findImagesByKeyword(request.getParameter("q"));
    }
}
