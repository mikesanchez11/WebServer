package com.michaelsanchez.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelsanchez.models.ImageModel;
import com.michaelsanchez.models.ImageResultModel;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiHandler extends AbstractHandler {
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        ImageResultModel imageResult = getImageResult();
        response.getWriter().println(convertToJson(imageResult));
        baseRequest.setHandled(true); // doesn't matter
    }

    private String convertToJson(ImageResultModel imageResult) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString =  objectMapper.writeValueAsString(imageResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private ImageResultModel getImageResult() {
        ImageResultModel imageResultModel = new ImageResultModel();
        ImageModel imageModel = null;

        try {
            imageModel = new ImageModel("this is the id", "this is the title", new URI("yahoo.com"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return imageResultModel.addImage(imageModel);
    }
}
