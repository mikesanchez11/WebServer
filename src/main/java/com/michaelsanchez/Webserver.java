package com.michaelsanchez;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.michaelsanchez.exceptions.ServerNotStartingException;
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


    public static void main(String[] args) throws ServerNotStartingException {
        Injector injector = Guice.createInjector(new ApiHandlerModule(), new FlickrClientModule());

        ApiHandler apiHandler = injector.getInstance(ApiHandler.class);
        apiHandler.setInjector(injector);

        Server server = injector.getInstance(Server.class);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(DEFAULT_PORT);
        server.addConnector(connector);
        server.setHandler(getHandler(apiHandler));

        try {
            server.start();
        } catch (Exception e) {
            throw new ServerNotStartingException(e);
        }
    }

    static Handler getHandler(ApiHandler handler) {
        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setResourceBase(".");
        contextHandler.setHandler(handler);
        return contextHandler;
    }
}
