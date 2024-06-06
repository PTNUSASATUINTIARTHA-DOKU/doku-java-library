package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
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
        var requestHeader = vaService.createVaRequestHeaderDto(createVaRequestDto, clientId, tokenB2b, privateKey);
        return vaService.createVa(requestHeader, createVaRequestDto, isProduction);
    }

    public CreateVaRequestDto convertToCreateVaRequestDto(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        return vaService.convertToCreateVaRequestDto(createVaRequestDtoV1);
    }
}
