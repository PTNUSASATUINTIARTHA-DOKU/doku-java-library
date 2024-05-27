package com.doku.sdk.dokujavalibrary.module;

import com.doku.sdk.dokujavalibrary.controller.TokenController;
import com.doku.sdk.dokujavalibrary.controller.VaController;
import com.doku.sdk.dokujavalibrary.dto.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
@RequiredArgsConstructor
public class DokuSnap {

    private final TokenController tokenController;
    private final VaController vaController;

    // global variable?
    private static PrivateKey privateKey;
    private static String clientId;
    private static Boolean isProduction;
    private static String tokenB2b;
    private static long tokenExpiresIn;
    private static long tokenGeneratedTimestamp;

    public TokenB2BResponseDto getB2bToken(PrivateKey privateKey, String clientId, Boolean isProduction) {
        return tokenController.getTokenB2B(privateKey, clientId, isProduction);
    }

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, String tokenB2b, PrivateKey privateKey, String clientId, Boolean isProduction) {

        Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
        if (tokenInvalid) {
            tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
        }

        return vaController.createVa(createVaRequestDto, privateKey, clientId, tokenB2b, isProduction);
    }
}
