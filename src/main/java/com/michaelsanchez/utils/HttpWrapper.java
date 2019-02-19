package com.michaelsanchez.utils;

import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

public class HttpWrapper {

    private Request baseRequest;
    private HttpServletResponse response;

    public HttpWrapper(Request baseRequest, HttpServletResponse response) {
        this.baseRequest = baseRequest;
        this.response = response;
    }

    public void setStatus(int status) {
        response.setStatus(status);
        baseRequest.setHandled(true);
    }

    public int getStatus() {
        return response.getStatus();
    }
}
