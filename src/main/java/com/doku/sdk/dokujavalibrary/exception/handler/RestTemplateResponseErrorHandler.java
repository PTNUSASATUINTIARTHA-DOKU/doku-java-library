package com.doku.sdk.dokujavalibrary.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Slf4j
@Component
public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().series() == SERVER_ERROR) {
            // handle SERVER_ERROR
            log.warn("#HttpServerErrorException [{}], will be handle by business service ", httpResponse.getRawStatusCode());
        } else if (httpResponse.getStatusCode().series() == CLIENT_ERROR) {
            // handle CLIENT_ERROR
            log.warn("#HttpClientErrorException [{}], will be handle by business service ", httpResponse.getRawStatusCode());
        }
    }
}
