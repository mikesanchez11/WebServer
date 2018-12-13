package com.michaelsanchez;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.michaelsanchez.exceptions.FlickrClientException;
import com.michaelsanchez.handlers.ApiHandler;
import com.michaelsanchez.models.FlickrResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FlickrClient {

    private static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    private static final String API_KEY_KEY = "api_key";
    private static final String BASE_URL = "https://api.flickr.com/services/rest/?";
    private static final String CALLBACK = "1";
    private static final String FORMAT = "json";
    private static final String FORMAT_KEY = "format";
    private static final String JSONCALLBACK_KEY = "jsoncallback";
    private static final String PHOTO_SEARCH_METHOD = "flickr.photos.search";

    private final ObjectMapper mapper;
    private CloseableHttpClient httpClient;

    private static Logger LOGGER = LoggerFactory.getLogger(FlickrClient.class);

    @Inject
    public FlickrClient(CloseableHttpClient httpClient, ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper;
    }


    public FlickrResponse findImagesByKeyword(String keyword) throws FlickrClientException {
        LOGGER.trace("Searching for {}", keyword);

        List<NameValuePair> namedValuePair = getNamedValuePair();
        namedValuePair.add(new BasicNameValuePair("method", PHOTO_SEARCH_METHOD));
        namedValuePair.add(new BasicNameValuePair("text", keyword));

        String params = makingUrlEncoded(namedValuePair);
        String uri = BASE_URL + params;

        LOGGER.debug("Hitting flickr {}", uri);

        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            String jsonResponse = EntityUtils.toString(entity);
            LOGGER.debug("flickr response: {}", jsonResponse);

            String updatedResponse = jsonResponse.replaceFirst("1\\(", "");
            updatedResponse = updatedResponse.substring(0, updatedResponse.length() - 1);

            EntityUtils.consume(entity);

            return mapper.readValue(updatedResponse, FlickrResponse.class);
        } catch (IOException e) {
            LOGGER.error("Error searching for {}", keyword, e);
            throw new FlickrClientException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String makingUrlEncoded(List<NameValuePair> namedValuePair) {
        if (namedValuePair.isEmpty()) return "";

        try {
            return EntityUtils.toString(new UrlEncodedFormEntity(namedValuePair, Charset.forName("UTF-8")));
        } catch (IOException e) {
            LOGGER.error("Couldn't URL encode {}", namedValuePair, e);
            throw new RuntimeException("get request param error");
        }
    }

    private List<NameValuePair> getNamedValuePair() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(API_KEY_KEY, API_KEY));
        nvps.add(new BasicNameValuePair(FORMAT_KEY, FORMAT));
        nvps.add(new BasicNameValuePair(JSONCALLBACK_KEY, CALLBACK));
        return nvps;
    }
}
