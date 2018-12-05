package com.michaelsanchez;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.michaelsanchez.exceptions.FlickrClientException;
import com.michaelsanchez.models.FlickrResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FlickrClient {

    private static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    private static final String CALLBACK ="1";
    private static final String FORMAT = "json";
    private static final String PHOTO_SEARCH_METHOD = "flickr.photos.search";
    private static final String BASE_URL = "https://api.flickr.com/services/rest/?";

    private final ObjectMapper mapper;
    private CloseableHttpClient httpClient;

    @Inject
    public FlickrClient(CloseableHttpClient httpClient, ObjectMapper mapper) {
         this.httpClient = httpClient;
         this.mapper = mapper;
    }


    public FlickrResponse findImagesByKeyword(String keyword) throws FlickrClientException {
        List<NameValuePair> namedValuePair = getNamedValuePair();
        namedValuePair.add(new BasicNameValuePair("method", PHOTO_SEARCH_METHOD));
        namedValuePair.add(new BasicNameValuePair("text", keyword));

        String params = makingUrlEncoded(namedValuePair);

        HttpGet httpGet = new HttpGet(BASE_URL + params);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            String jsonResponse = EntityUtils.toString(entity);

            String updatedResponse = jsonResponse.replaceFirst("1\\(", "");
            updatedResponse = updatedResponse.substring(0, updatedResponse.length()-1);

            EntityUtils.consume(entity);
            response.close();

            return mapper.readValue(updatedResponse, FlickrResponse.class);
        } catch (IOException e) {
            throw new FlickrClientException(e);
        }
    }

    private String makingUrlEncoded(List<NameValuePair> namedValuePair) {
        String param = "";
        if (!namedValuePair.isEmpty()) {
            try {
                param = EntityUtils.toString(new UrlEncodedFormEntity(namedValuePair, Charset.forName("UTF-8")));
            } catch (IOException e) {
                // THROW A BETTER EXCEPTION
                throw new RuntimeException("get request param error");
            }
        }
        return param;
    }

    private List<NameValuePair> getNamedValuePair() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("api_key", API_KEY));
        nvps.add(new BasicNameValuePair("format", FORMAT));
        nvps.add(new BasicNameValuePair("jsoncallback", CALLBACK));
        return nvps;
    }
}
