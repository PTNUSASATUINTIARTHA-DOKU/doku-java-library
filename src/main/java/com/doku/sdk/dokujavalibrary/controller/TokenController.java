package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.common.SnapUtils;
import com.doku.sdk.dokujavalibrary.dto.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.token.request.TokenB2B2CRequestDto;
import com.doku.sdk.dokujavalibrary.dto.token.request.TokenB2BRequestDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2B2CResponseDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenDto;
import com.doku.sdk.dokujavalibrary.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenController {

    private final TokenService tokenService;
    private final SnapUtils snapUtils;

    public TokenB2BResponseDto getTokenB2B(String privateKey, String clientId, Boolean isProduction) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateAsymmetricSignature(privateKey, clientId, timestamp);
        TokenB2BRequestDto tokenB2BRequestDTO = tokenService.createTokenB2BRequestDTO(signature, clientId, timestamp);
        return tokenService.createTokenB2B(tokenB2BRequestDTO, isProduction);
    }

    public TokenB2B2CResponseDto getTokenB2B2C(String authCode, String privateKey, String clientId, Boolean isProduction) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateAsymmetricSignature(privateKey, clientId, timestamp);
        TokenB2B2CRequestDto tokenB2B2CRequestDto = tokenService.createTokenB2b2cRequestDto(authCode);
        return tokenService.hitTokenB2b2cApi(tokenB2B2CRequestDto, timestamp, signature, clientId, isProduction);
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

    public Boolean validateAsymmetricSignature(String requestSignature, String requestTimestamp, String privateKey, String clientId) {
        String newSignature = tokenService.generateAsymmetricSignature(privateKey, clientId, requestTimestamp);
        return tokenService.compareSignatures(requestSignature, newSignature);
    }

    public Boolean validateTokenB2b(String requestTokenB2b, String publicKey) {
        return tokenService.validateTokenB2b(requestTokenB2b, publicKey);
    }

    public NotificationTokenDto generateTokenB2b(long expiredIn, String issuer, String privateKey, String clientId, String timestamp) {
        String token = tokenService.generateToken(expiredIn, issuer, clientId, privateKey);
        return tokenService.generateNotificationTokenDto(token, timestamp, clientId, expiredIn);
    }

    public NotificationTokenDto generateInvalidSignatureResponse() {
        String timestamp = tokenService.getTimestamp();
        return tokenService.generateInvalidSignature(timestamp);
    }

    public RequestHeaderDto doGenerateRequestHeader(String privateKey, String clientId, String tokenB2b) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateAsymmetricSignature(privateKey, clientId, timestamp);
        String externalId = snapUtils.generateExternalId();

        return snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null, null, null, tokenB2b);
    }
}
