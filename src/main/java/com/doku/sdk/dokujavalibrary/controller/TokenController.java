package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.dto.request.TokenB2BRequestDto;
import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenController {

    private final TokenService tokenService;

    public TokenB2BResponseDto getTokenB2B(PrivateKey privateKey, String clientId, Boolean isProduction) {
        log.debug("Get Token B2B...");

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.createSignature(privateKey, clientId, timestamp);
        TokenB2BRequestDto tokenB2BRequestDTO = tokenService.createTokenB2BRequestDTO(signature, clientId, timestamp);
        return tokenService.createTokenB2B(tokenB2BRequestDTO, isProduction);
    }
}
