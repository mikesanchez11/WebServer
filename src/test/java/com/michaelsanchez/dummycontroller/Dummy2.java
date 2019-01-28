package com.michaelsanchez.dummycontroller;

import com.michaelsanchez.anotations.API;
import com.michaelsanchez.controllers.Controller;

import javax.servlet.http.HttpServletRequest;

@API("/api/dummy2")
public class Dummy2 implements Controller {
    @Override
    public Object handleRequest(HttpServletRequest request) throws Throwable {
        return null;
    }
}
