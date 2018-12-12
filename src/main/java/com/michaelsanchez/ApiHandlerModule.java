package com.michaelsanchez;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.eclipse.jetty.server.Server;

public class ApiHandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
    }

    @Provides
    public ObjectWriter provideObjectWriter() {
        return new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @Provides
    public Server providesServer() {
        return new Server();
    }
}