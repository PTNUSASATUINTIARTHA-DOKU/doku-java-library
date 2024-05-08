package com.doku.sdk.dokujavalibrary.service;

import com.doku.au.security.module.snap.AccessTokenB2bSignatureComponentDTO;
import com.doku.au.security.module.snap.SnapSignatureService;
import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.common.DateUtils;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.request.TokenB2BRequestDto;
import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final DateUtils dateUtils;
    private final ConnectionUtils connectionUtils;
    private final SnapSignatureService snapSignatureService;
    private final Gson gson;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Value("${doku-sdk.snap.generate-token-b2b}")
    private String accessTokenUrl;

    @Value("${doku-sdk.snap.sandbox-base-url}")
    private String sandboxBaseUrl;

    @Value("${doku-sdk.snap.production-base-url}")
    private String productionBaseUrl;

    public String getTimestamp() {
        return dateUtils.getISO8601StringFromDateUTC(LocalDateTime.now(), dateTimeFormatter);
    }

    @SneakyThrows
    public String createSignature(PrivateKey privateKey, String clientId, String timestamp) {
        var accessTokenB2bSignatureComponentDTO = AccessTokenB2bSignatureComponentDTO.builder()
                .privateKey(privateKey)
                .clientKey(clientId)
                .timestamp(timestamp)
                .build();

        return snapSignatureService.createAccessTokenB2bSignature(accessTokenB2bSignatureComponentDTO);
    }

    public TokenB2BRequestDto createTokenB2BRequestDTO(String signature, String clientId, String timestamp) {
        return TokenB2BRequestDto.builder()
                .signature(signature)
                .timestamp(timestamp)
                .clientId(clientId)
                .grantType("client_credentials")
                .build();
    }

    @SneakyThrows
    public TokenB2BResponseDto createTokenB2B(TokenB2BRequestDto tokenB2BRequestDTO, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, tokenB2BRequestDTO.getTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_CLIENT_KEY, tokenB2BRequestDTO.getClientId());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, tokenB2BRequestDTO.getSignature());

        StringBuilder url = new StringBuilder();
        if (isProduction) {
            url.append(productionBaseUrl);
            url.append(accessTokenUrl);
        } else {
            url.append(sandboxBaseUrl);
            url.append(accessTokenUrl);
        }

        var response = connectionUtils.httpPost(url.toString(), httpHeaders, gson.toJson(tokenB2BRequestDTO));

        return gson.fromJson(response.getBody(), TokenB2BResponseDto.class);
    }
}
