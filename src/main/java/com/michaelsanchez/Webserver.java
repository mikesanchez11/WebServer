package com.michaelsanchez;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.michaelsanchez.handlers.ApiHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Webserver {
    private static final int DEFAULT_PORT = 8080;

    private static Logger LOGGER = LoggerFactory.getLogger(Webserver.class);


    public static void main(String[] args) throws Exception {
        Server server = new Server();

        Injector injector = Guice.createInjector(new ApiHandlerModule(), new FlickrClientModule());
        ApiHandler apiHandler = injector.getInstance(ApiHandler.class);
        apiHandler.setInjector(injector);


        ServerConnector connector = new ServerConnector(server);
        connector.setPort(DEFAULT_PORT);
        server.addConnector(connector);
        server.setHandler(getHandler(apiHandler));

        server.start();
    }

    static Handler getHandler(ApiHandler handler) {

        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setResourceBase(".");
        contextHandler.setHandler(handler);
        return contextHandler;
    }
}
