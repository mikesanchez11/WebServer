package com.michaelsanchez.controllers;

import javax.servlet.http.HttpServletRequest;

public interface Controller {
    public Object handleRequest(HttpServletRequest request) throws Throwable;
}
