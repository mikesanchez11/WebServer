package com.michaelsanchez.controllers;

import javax.servlet.http.HttpServletRequest;

public interface Controller {
    Object handleRequest(HttpServletRequest request) throws Throwable;
}
