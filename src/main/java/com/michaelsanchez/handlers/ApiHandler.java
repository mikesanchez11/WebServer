package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelsanchez.exceptions.JsonConversionException;
import com.michaelsanchez.exceptions.ModelCreationException;
import com.michaelsanchez.models.ImageModel;
import com.michaelsanchez.models.ImageResultModel;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiHandler extends AbstractHandler {
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ImageResultModel imageResult = getImageResult();
            response.getWriter().println(convertToJson(imageResult));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ModelCreationException | JsonConversionException e) {
            response.getWriter().println(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            baseRequest.setHandled(true);
        }
    }

    private String convertToJson(ImageResultModel imageResult) throws JsonConversionException {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(imageResult);
        } catch (JsonProcessingException e) {
            throw new JsonConversionException(e);
        }
    }

    private ImageResultModel getImageResult() throws ModelCreationException {
        try {
            ImageResultModel imageResultModel = new ImageResultModel();
            ImageModel imageModel = new ImageModel("this is the id", "this is the title", new URI("yahoo.com"));
            ImageModel imageModel1 = new ImageModel("this is the id1", "this is the title", new URI("yahoo.com"));
            ImageModel imageModel2 = new ImageModel("this is the id2", "this is the title", new URI("yahoo.com"));

           return imageResultModel.addImage(imageModel)
                   .addImage(imageModel1)
                   .addImage(imageModel2);
        } catch (URISyntaxException e) {
            throw new ModelCreationException(e);
        }
    }
}
