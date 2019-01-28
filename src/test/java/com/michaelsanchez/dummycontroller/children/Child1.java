package com.michaelsanchez.dummycontroller.children;

import com.michaelsanchez.anotations.API;
import com.michaelsanchez.controllers.Controller;

import javax.servlet.http.HttpServletRequest;

@API("/api/child")
public class Child1 implements Controller {
    @Override
    public Object handleRequest(HttpServletRequest request) throws Throwable {
        return null;
    }
}
