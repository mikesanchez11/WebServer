package com.michaelsanchez.controllers;

import com.michaelsanchez.BingResponse;

import javax.servlet.http.HttpServletRequest;

public class BingController implements Controller {

    @Override
    public Object handleRequest(HttpServletRequest request) throws Throwable {
        return new BingResponse();
    }
}
