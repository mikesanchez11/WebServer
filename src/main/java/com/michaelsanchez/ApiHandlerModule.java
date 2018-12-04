package com.michaelsanchez;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ApiHandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
    }

    @Provides
    public ObjectWriter provideObjectWriter() {
        return new ObjectMapper().writerWithDefaultPrettyPrinter();
    }
}