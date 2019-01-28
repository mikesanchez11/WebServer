package com.michaelsanchez.dummycontroller;

import com.michaelsanchez.controllers.Controller;

import javax.servlet.http.HttpServletRequest;

public class Dummy3 implements Controller {
    @Override
    public Object handleRequest(HttpServletRequest request) throws Throwable {
        return null;
    }
}
