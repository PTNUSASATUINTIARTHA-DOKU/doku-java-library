package com.doku.sdk.dokujavalibrary.config;

import com.doku.sdk.dokujavalibrary.exception.handler.RestTemplateResponseErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${doku-sdk.proxy.host}")
    private String proxyHost;

    @Value("${doku-sdk.proxy.port}")
    private Integer proxyPort;

    @Value("${doku-sdk.http.client.proxy.enable}")
    private Boolean enableProxy;

    @Value("${doku-sdk.read.timeout}")
    private Integer readTimeOut;

    @Value("${doku-sdk.connect.timeout}")
    private Integer connectTimeOut;

    @Value("${doku-sdk.socket.timeout}")
    private Integer socketTimeOut;

    @Value("${doku-sdk.connection-request.timeout}")
    private Integer connectionRequestTimeOut;

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
