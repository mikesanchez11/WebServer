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
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Webserver {
    private static final int DEFAULT_PORT = 8080;

    private static Logger LOGGER = LoggerFactory.getLogger(Webserver.class);
    private static Marker FATAL = MarkerFactory.getMarker("FATAL");

    Injector injector;
    private Server server;

    public Webserver(Injector injector) {
        this.injector = injector;
    }

    public static void main(String[] args) throws ServerNotStartingException {
        Injector injector = Guice.createInjector(new ApiHandlerModule(), new FlickrClientModule());

        Webserver webserver = new Webserver(injector);
        Server serverForPort = webserver.createServerForPort(DEFAULT_PORT);
        webserver.setServer(serverForPort);

        ApiHandler apiHandler = injector.getInstance(ApiHandler.class);
        apiHandler.setInjector(injector);

        Handler handler = webserver.getHandler(apiHandler);
        webserver.server.setHandler(handler);

        webserver.startServer(webserver.server);
    }

    void setServer(Server server) {
        this.server = server;
    }

    void startServer(Server server) throws ServerNotStartingException {
        try {
            LOGGER.info("Starting on {}", DEFAULT_PORT);
            server.start();
            LOGGER.info("Server Started");
        } catch (Throwable e) {
            LOGGER.error(FATAL,"Error Starting Server", e);
            throw new ServerNotStartingException(e);
        }
    }

    Handler getHandler(ApiHandler handler) {
        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setResourceBase(".");
        contextHandler.setHandler(handler);
        return contextHandler;
    }

    Server createServerForPort(int port) {
        Server server = injector.getInstance(Server.class);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);
        return server;
    }

    public boolean isStarted() {
        return server.isStarted();
    }
}
