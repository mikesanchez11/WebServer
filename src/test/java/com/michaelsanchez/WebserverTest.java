package com.michaelsanchez;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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


//    @Test
//    public void whenConnectionHasValidHandler_shouldThrowOKResponseCode() throws Exception {
//        server.setHandler(getHandler());
//
//        server.start();
//        HttpURLConnection http = (HttpURLConnection)new URL("http://localhost:8080/").openConnection();
//        http.connect();
//        assertEquals(HttpURLConnection.HTTP_OK, http.getResponseCode());
//    }
//
//    @Test
//    public void whenConnectionHasInvalidHandler_shouldThrow() throws Exception {
//        server.setHandler(new ContextHandler("/Invalid"));
//
//        server.start();
//        HttpURLConnection http = (HttpURLConnection)new URL("http://localhost:8080/").openConnection();
//        http.connect();
//        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, http.getResponseCode());
//    }
}
