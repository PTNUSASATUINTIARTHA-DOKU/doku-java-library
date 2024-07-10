package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.doku.sdk.dokujavalibrary.service.TokenService;
import com.doku.sdk.dokujavalibrary.service.VaService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaController {

    private final VaService vaService;
    private final TokenService tokenService;
    private final Gson gson;

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, PrivateKey privateKey, String clientId, String tokenB2b, Boolean isProduction) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.createSignature(privateKey, clientId, timestamp);
        String externalId = vaService.generateExternalId();
        String channelId = "SDK";
        var requestHeader = vaService.generateRequestHeaderDto(timestamp, signature, clientId, externalId, channelId, tokenB2b);

        return vaService.createVa(requestHeader, createVaRequestDto, isProduction);
    }

    public CreateVaRequestDto convertToCreateVaRequestDto(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        return vaService.convertToCreateVaRequestDto(createVaRequestDtoV1);
    }

    public UpdateVaResponseDto doUpdateVa(UpdateVaDto updateVaDto, String clientId, String tokenB2b, String secretKey, Boolean isProduction) {
        String endpointUrl = SdkConfig.getUpdateVaUrl(isProduction);
        String requestBody = gson.toJson(updateVaDto);
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.PUT.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = vaService.generateExternalId();
        String channelId = "SDK";
        var requestHeader = vaService.generateRequestHeaderDto(timestamp, signature, clientId, externalId, channelId, tokenB2b);

        return vaService.doUpdateVa(requestHeader, updateVaDto, isProduction);
    }

    public DeleteVaResponseDto doDeletePaymentCode(DeleteVaRequestDto deleteVaRequestDto, String clientId, String tokenB2b, String secretKey, Boolean isProduction) {
        String endpointUrl = SdkConfig.getDeleteVaUrl(isProduction);
        String requestBody = gson.toJson(deleteVaRequestDto);
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.DELETE.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = vaService.generateExternalId();
        String channelId = "SDK";
        var requestHeader = vaService.generateRequestHeaderDto(timestamp, signature, clientId, externalId, channelId, tokenB2b);

        return vaService.doDeletePaymentCode(requestHeader, deleteVaRequestDto, isProduction);
    }

    public CheckStatusVaResponseDto doCheckStatusVa(CheckStatusVaRequestDto checkStatusVaRequestDto, String clientId, String tokenB2b, String secretKey, Boolean isProduction) {
        String endpointUrl = SdkConfig.getCheckStatusVaUrl(isProduction);
        String requestBody = gson.toJson(checkStatusVaRequestDto);
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = vaService.generateExternalId();
        var requestHeader = vaService.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, tokenB2b);

        return vaService.doCheckStatusVa(requestHeader, checkStatusVaRequestDto, isProduction);
    }
}
