package com.doku.sdk.dokujavalibrary.config;

import com.doku.sdk.dokujavalibrary.exception.handler.RestTemplateResponseErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private static final String proxyHost = "localhost";
    private static final Integer proxyPort = 8443;
    private static final Boolean enableProxy = false;
    private static final Integer readTimeOut = 30000;
    private static final Integer connectTimeOut = 30000;
    private static final Integer socketTimeOut = 30000;
    private static final Integer connectionRequestTimeOut = 30000;

    private final RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    /**
     * Instantiated Object Rest template without proxy.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate() {

        log.info("Common Config Rest Template");
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeOut)
                .setConnectionRequestTimeout(connectionRequestTimeOut)
                .setSocketTimeout(socketTimeOut).build();

        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(config);
        final CloseableHttpClient client = clientBuilder.build();

        return generateRestTemplate(client);
    }

    private RestTemplate generateRestTemplate(CloseableHttpClient client){
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);
        factory.setReadTimeout(readTimeOut);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        restTemplate.setErrorHandler(restTemplateResponseErrorHandler);
        return restTemplate;
    }

    @Bean(name = "restTemplateWithProxy")
    public RestTemplate restTemplateWithProxy() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeOut)
                .setConnectionRequestTimeout(connectionRequestTimeOut)
                .setSocketTimeout(socketTimeOut).build();

        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(config);
        final CloseableHttpClient client = clientBuilder.build();

        if(enableProxy) {
            log.info("Using Rest Template With Proxy");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setProxy(proxy);

            RestTemplate restTemplate = generateRestTemplate(client);
            restTemplate.setRequestFactory(requestFactory);
            return restTemplate;
        }
        return generateRestTemplate(client);
    }
}
