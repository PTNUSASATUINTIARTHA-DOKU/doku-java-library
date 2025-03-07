package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.common.SnapUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.request.InquiryRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.doku.sdk.dokujavalibrary.service.TokenService;
import com.doku.sdk.dokujavalibrary.service.VaService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaController {

    private final VaService vaService;
    private final TokenService tokenService;
    private final SnapUtils snapUtils;
    private final Gson gson;

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, String clientId, String tokenB2b, String secretKey, Boolean isProduction) {
        String endpointUrl = SdkConfig.getCreateVaUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        createVaRequestDto.setOrigin(CreateVaRequestDto.OriginDto.builder()
                .product("SDK")
                .source("Java")
                .sourceVersion(SdkConfig.SDK_VERSION)
                .system("doku-java-library")
                .apiFormat("SNAP")
                .build());
        String requestBody = gson.toJson(createVaRequestDto);
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();
        String channelId = "SDK";
        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null, channelId, null, tokenB2b);

        return vaService.createVa(requestHeader, createVaRequestDto, isProduction);
    }

    public CreateVaRequestDto convertToCreateVaRequestDto(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        return vaService.convertToCreateVaRequestDto(createVaRequestDtoV1);
    }

    public UpdateVaResponseDto doUpdateVa(UpdateVaRequestDto updateVaRequestDto, String clientId, String tokenB2b, String secretKey, Boolean isProduction) {
        String endpointUrl = SdkConfig.getUpdateVaUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(updateVaRequestDto);
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.PUT.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();
        String channelId = "SDK";
        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null, channelId, null, tokenB2b);

        return vaService.doUpdateVa(requestHeader, updateVaRequestDto, isProduction);
    }

    public DeleteVaResponseDto doDeletePaymentCode(DeleteVaRequestDto deleteVaRequestDto, String clientId, String tokenB2b, String secretKey, Boolean isProduction) {
        String endpointUrl = SdkConfig.getDeleteVaUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(deleteVaRequestDto);
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.DELETE.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();
        String channelId = "SDK";
        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null, channelId, null, tokenB2b);

        return vaService.doDeletePaymentCode(requestHeader, deleteVaRequestDto, isProduction);
    }

    public CheckStatusVaResponseDto doCheckStatusVa(CheckStatusVaRequestDto checkStatusVaRequestDto, String clientId, String tokenB2b, String secretKey, Boolean isProduction) {
        String endpointUrl = SdkConfig.getCheckStatusVaUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(checkStatusVaRequestDto);
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();
        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null,null, null, tokenB2b);

        return vaService.doCheckStatusVa(requestHeader, checkStatusVaRequestDto, isProduction);
    }

    public InquiryResponseBodyDto v1ToSnapConverter(String inquiryResponseV1) {
        return vaService.directInquiryResponseMapping(inquiryResponseV1);
    }

    public String snapToV1Converter(HttpServletRequest headerRequest, InquiryRequestBodyDto inquiryRequestBodyDto) {
        return vaService.directInquiryRequestMapping(headerRequest, inquiryRequestBodyDto);
    }

    public String vaPaymentNotificationConverter(PaymentNotificationRequestBodyDto paymentNotificationRequestBodyDto) {
        return vaService.vaPaymentNotificationConverter(paymentNotificationRequestBodyDto);
    }
}
