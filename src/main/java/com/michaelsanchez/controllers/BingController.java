package com.michaelsanchez.controllers;

import com.michaelsanchez.BingResponse;
import com.michaelsanchez.anotations.API;

import javax.servlet.http.HttpServletRequest;

@API("/api/bing")
public class BingController implements Controller {

    @Override
    public Object handleRequest(HttpServletRequest request) {
        return new BingResponse();
    }
}
