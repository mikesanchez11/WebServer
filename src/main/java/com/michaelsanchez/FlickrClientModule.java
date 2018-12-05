package com.michaelsanchez;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class FlickrClientModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
    }

    @Provides
    public ObjectMapper providesObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    public CloseableHttpClient providesHttpClient() {
        return HttpClients.createDefault();
    }
}
