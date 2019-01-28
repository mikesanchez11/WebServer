package com.michaelsanchez.dummycontroller;

import com.michaelsanchez.anotations.API;
import com.michaelsanchez.controllers.Controller;

import javax.servlet.http.HttpServletRequest;

@API("/api/dummy1")
public class Dummy1 implements Controller {
    @Override
    public Object handleRequest(HttpServletRequest request) throws Throwable {
        return null;
    }
}
