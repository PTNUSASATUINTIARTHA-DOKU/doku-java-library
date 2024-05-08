package com.doku.sdk.dokujavalibrary.common;

import com.doku.sdk.dokujavalibrary.exception.TimeoutException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionUtils {

    private final RestTemplate restTemplateWithProxy;

    public ResponseEntity<String> httpPost(
            String url, HttpHeaders headers, String request) {
        log.debug("URL {}", url);
        log.debug("Param {}", request);
        log.debug("Headers {}", headers != null ? headers.toSingleValueMap() : "");

        URI uri = URI.create(url.trim());

        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplateWithProxy.exchange(uri, HttpMethod.POST, requestEntity, String.class);
            log.debug("Response Header: {}", new Gson().toJson(responseEntity.getHeaders()));
            log.debug("Response Body: {} ", responseEntity.getBody());
            return responseEntity;
        } catch (ResourceAccessException ex) {
            log.warn("Gateway Time Out To Destination Host : {}", ex.getMessage());
            throw new TimeoutException("00", "Timeout");
        }
    }
}
