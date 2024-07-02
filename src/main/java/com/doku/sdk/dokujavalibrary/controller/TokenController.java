package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.request.TokenB2BRequestDto;
import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenDto;
import com.doku.sdk.dokujavalibrary.service.TokenService;
import com.doku.sdk.dokujavalibrary.service.VaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenController {

    private final TokenService tokenService;
    private final VaService vaService;

    public TokenB2BResponseDto getTokenB2B(PrivateKey privateKey, String clientId, Boolean isProduction) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.createSignature(privateKey, clientId, timestamp);
        TokenB2BRequestDto tokenB2BRequestDTO = tokenService.createTokenB2BRequestDTO(signature, clientId, timestamp);
        return tokenService.createTokenB2B(tokenB2BRequestDTO, isProduction);
    }

    public Boolean isTokenInvalid(String tokenB2b, long tokenExpiresIn, long tokenGeneratedTimestamp) {
        if (tokenService.isTokenEmpty(tokenB2b)) {
            return true;
        } else {
            if (tokenService.isTokenExpired(tokenExpiresIn, tokenGeneratedTimestamp)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Boolean validateSignature(String requestSignature, String requestTimestamp, PrivateKey privateKey, String clientId) {
        String newSignature = tokenService.createSignature(privateKey, clientId, requestTimestamp);
        return tokenService.compareSignatures(requestSignature, newSignature);
    }

    public Boolean validateTokenB2b(String requestTokenB2b, String publicKey) {
        return tokenService.validateTokenB2b(requestTokenB2b, publicKey);
    }

    public NotificationTokenDto generateTokenB2b(long expiredIn, String issuer, PrivateKey privateKey, String clientId, String timestamp) {
        String token = tokenService.generateToken(expiredIn, issuer, clientId, privateKey);
        return tokenService.generateNotificationTokenDto(token, timestamp, clientId, expiredIn);
    }

    public NotificationTokenDto generateInvalidSignatureResponse() {
        String timestamp = tokenService.getTimestamp();
        return tokenService.generateInvalidSignature(timestamp);
    }

    public RequestHeaderDto doGenerateRequestHeader(PrivateKey privateKey, String clientId, String tokenB2b) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.createSignature(privateKey, clientId, timestamp);
        String externalId = vaService.generateExternalId();

        return vaService.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, tokenB2b);
    }
}
