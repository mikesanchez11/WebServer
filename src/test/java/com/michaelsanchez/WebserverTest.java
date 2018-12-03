package com.michaelsanchez;


import com.michaelsanchez.handlers.ApiHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class WebserverTest {
    private static final int DEFAULT_PORT = 8080;

    private Server server;

    @Before
    public void StartJettyServer() {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(DEFAULT_PORT);
        server.addConnector(connector);
    }

    @After
    public void stopJetty()
    {
        try
        {
            server.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void whenConnectionHasValidHandler_shouldThrowOKResponseCode() throws Exception {
        server.setHandler(getHandler());

        server.start();
        HttpURLConnection http = (HttpURLConnection)new URL("http://localhost:8080/").openConnection();
        http.connect();
        assertEquals(HttpURLConnection.HTTP_OK, http.getResponseCode());
    }

    @Test
    public void whenConnectionHasInvalidHandler_shouldThrow() throws Exception {
        server.setHandler(new ContextHandler("/Invalid"));

        server.start();
        HttpURLConnection http = (HttpURLConnection)new URL("http://localhost:8080/").openConnection();
        http.connect();
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, http.getResponseCode());
    }

    private static Handler getHandler() {
        ApiHandler handler = new ApiHandler();

        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setResourceBase(".");
        contextHandler.setHandler(handler);
        return contextHandler;
    }
}
