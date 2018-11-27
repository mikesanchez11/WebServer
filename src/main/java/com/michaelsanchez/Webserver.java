package com.michaelsanchez;

import com.michaelsanchez.handlers.ApiHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class Webserver {

    public static final int SERVER_DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        final Server server = new Server();
        //registerShutdown(server);

        // setup the connector
        final ServerConnector connector0 = new ServerConnector(server);
        connector0.setPort(SERVER_DEFAULT_PORT);
        server.setConnectors(new Connector[]{connector0});
        server.setHandler(getContextHandler());

        // lets do this
        server.setStopAtShutdown(true);

        //TODO: handle execpetions
        server.start();
        server.join();
    }

    private static ContextHandlerCollection getContextHandler() {
        final ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setResourceBase(".");
        contextHandler.setHandler(new ApiHandler());
        contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { contextHandler});


        return contexts;
    }

}
