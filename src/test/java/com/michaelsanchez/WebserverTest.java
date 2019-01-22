package com.michaelsanchez;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.michaelsanchez.exceptions.ServerNotStartingException;
import com.michaelsanchez.handlers.ApiHandler;
import org.eclipse.jetty.server.AbstractNetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebserverTest {
    private static Webserver webserver;

    @BeforeClass
    public static void instantiateWebServer() {
        Injector injector = Guice.createInjector(new ApiHandlerModule(), new FlickrClientModule());
        webserver = new Webserver(injector);
    }

    @Test
    public void testGetHandler() {
        ApiHandler handler = new ApiHandler(null);
        ContextHandler webserverHandler = (ContextHandler) webserver.getHandler(handler);
        assertEquals(handler, webserverHandler.getHandler());
    }

    @Test
    public void testGetServerForPort() {
        int port = 9090;
        Server server = webserver.createServerForPort(port);

        AbstractNetworkConnector connector = (AbstractNetworkConnector) server.getConnectors()[0];
        assertEquals(port, connector.getPort());
    }

    @Test
    public void testStartingServer() {
        int port = 9090;
        Server server = webserver.createServerForPort(port);
        webserver.setServer(server);

        try {
            webserver.startServer(server);
            assertTrue(webserver.isStarted());
        } catch (ServerNotStartingException e) {
            e.printStackTrace();
        }
    }
}