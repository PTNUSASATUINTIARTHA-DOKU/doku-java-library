package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaService {

    private final TokenService tokenService;
    private final ConnectionUtils connectionUtils;
    private final Gson gson;

    public String generateExternalId() {
        UUID uuid = UUID.randomUUID();
        long timestamp = Instant.now().getEpochSecond();

        return uuid.toString() + timestamp;
    }

    public RequestHeaderDto generateRequestHeaderDto(String timestamp, String signature, String clientId, String externalId, String channelId, String tokenB2b) {
        return RequestHeaderDto.builder()
                .xTimestamp(timestamp)
                .xSignature(signature)
                .xPartnerId(clientId)
                .xExternalId(externalId)
                .channelId(channelId)
                .authorization(tokenB2b)
                .build();
    }

    public CreateVaResponseDto createVa(RequestHeaderDto requestHeaderDto, CreateVaRequestDto createVaRequestDto, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getCreateVaUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(createVaRequestDto));

        return gson.fromJson(response.getBody(), CreateVaResponseDto.class);
    }

    public CreateVaRequestDto convertToCreateVaRequestDto(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        return CreateVaRequestDto.builder()
                .partnerServiceId(createVaRequestDtoV1.getPartnerServiceId())
                .virtualAccountName(createVaRequestDtoV1.getName())
                .virtualAccountEmail(createVaRequestDtoV1.getEmail())
                .virtualAccountPhone(createVaRequestDtoV1.getMobilephone())
                .trxId(createVaRequestDtoV1.getTransIdMerchant())
                .totalAmount(TotalAmountDto.builder()
                        .value(createVaRequestDtoV1.getAmount())
                        .currency(createVaRequestDtoV1.getCurrency())
                        .build())
                .additionalInfo(AdditionalInfoDto.builder()
                        .channel(createVaRequestDtoV1.getPaymentChannel())
                        .build())
                .virtualAccountTrxType("1")
                .expiredDate(createVaRequestDtoV1.getExpiredDate())
                .build();
    }

    public UpdateVaResponseDto doUpdateVa(RequestHeaderDto requestHeaderDto, UpdateVaDto updateVaDto, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getUpdateVaUrl(isProduction);
        var response = connectionUtils.httpPut(url, httpHeaders, gson.toJson(updateVaDto));

        return gson.fromJson(response.getBody(), UpdateVaResponseDto.class);
    }

    public DeleteVaResponseDto doDeletePaymentCode(RequestHeaderDto requestHeaderDto, DeleteVaRequestDto deleteVaRequestDto, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDeleteVaUrl(isProduction);
        var response = connectionUtils.httpDelete(url, httpHeaders, gson.toJson(deleteVaRequestDto));

        return gson.fromJson(response.getBody(), DeleteVaResponseDto.class);
    }
}
