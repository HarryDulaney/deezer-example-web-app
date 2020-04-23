package com.hgdiv.opendata.utils;


import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hgdiv.opendata.model.Artist;
import com.hgdiv.opendata.model.Artists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class SIMHRestTemplate {
    Logger log = LoggerFactory.getLogger(SIMHRestTemplate.class);

    private HttpConnectionUtils httpConnectionUtils;

    public SIMHRestTemplate(HttpConnectionUtils httpConnectionUtils) {
        this.httpConnectionUtils = httpConnectionUtils;
    }

    public Artists get(final String url) throws RestClientException {
        return getRequest(url);
    }

    private Artists getRequest(final String url) throws RestClientException {

        try {
            final String response = httpConnectionUtils.getConnect(url);
            if (containsError(response)) {
                throw new RestClientException("Error " + response);
            } else {
                return convertJson(httpConnectionUtils.getConnect(url));
            }
        } catch (IOException ex) {
            throw new RestClientException("There is an exception for url " + url, ex);
        }
    }

    private Artists convertJson(final String content) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content,Artists.class);


    }

    private Boolean containsError(final String response) {
        return response.startsWith("{\"error");
    }
}

