package com.michaelsanchez;

import com.michaelsanchez.handlers.ApiHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;

public class Webserver {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(DEFAULT_PORT);
        server.addConnector(connector);
        server.setHandler(getHandler());

        server.start();
    }

    private static Handler getHandler() {
        ApiHandler handler = new ApiHandler();

        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setResourceBase(".");
        contextHandler.setHandler(handler);
        return contextHandler;
    }
}
