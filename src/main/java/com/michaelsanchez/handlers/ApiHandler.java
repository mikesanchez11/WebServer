package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelsanchez.exceptions.JsonConverstionException;
import com.michaelsanchez.models.ImageModel;
import com.michaelsanchez.models.ImageResultModel;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

public class ApiHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ImageResultModel imageResultModel = new ImageResultModel();
            imageResultModel.addImage(new ImageModel("id", "title", URI.create("yahoo.com")));

            response.getWriter().println(getJsonConversion(imageResultModel));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonConverstionException e) {
            response.getWriter().println(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            baseRequest.setHandled(true);
        }
    }

    private String getJsonConversion(ImageResultModel imageResultModel) throws JsonConverstionException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(imageResultModel);
        } catch (JsonProcessingException e) {
            throw new JsonConverstionException(e);
        }
    }
}
