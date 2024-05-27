package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.dto.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.service.VaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaController {

    private final VaService vaService;

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, PrivateKey privateKey, String clientId, String tokenB2b, Boolean isProduction) {
        log.info("createVa...");

        var requestHeader = vaService.createVaRequestHeaderDto(createVaRequestDto, clientId, tokenB2b, privateKey);
        return vaService.createVa(requestHeader, createVaRequestDto, isProduction);
    }
}
