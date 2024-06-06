package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaService {

    private final TokenService tokenService;
    private final ConnectionUtils connectionUtils;
    private final Gson gson;

    @Value("${doku-sdk.snap.create-va}")
    private String createVaUrl;

    @Value("${doku-sdk.snap.sandbox-base-url}")
    private String sandboxBaseUrl;

    @Value("${doku-sdk.snap.production-base-url}")
    private String productionBaseUrl;

    public String generateExternalId() {
        UUID uuid = UUID.randomUUID();
        long timestamp = Instant.now().getEpochSecond();

        return uuid.toString() + timestamp;
    }

    public RequestHeaderDto createVaRequestHeaderDto(CreateVaRequestDto createVaRequestDto, String clientId, String tokenB2b, PrivateKey privateKey) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.createSignature(privateKey, clientId, timestamp);

        return RequestHeaderDto.builder()
                .xTimestamp(timestamp)
                .xSignature(signature)
                .xPartnerId(clientId)
                .xExternalId(generateExternalId())
                .channelId(createVaRequestDto.getAdditionalInfo().getChannel())
                .authorization(tokenB2b)
                .build();
    }

    public CreateVaResponseDto createVa(RequestHeaderDto requestHeaderDto, CreateVaRequestDto createVaRequestDto, Boolean isProduction) {
        createVaRequestDto.validateCreateRequestVaDto(createVaRequestDto);

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        StringBuilder url = new StringBuilder();
        if (isProduction) {
            url.append(productionBaseUrl);
            url.append(createVaUrl);
        } else {
            url.append(sandboxBaseUrl);
            url.append(createVaUrl);
        }

        var response = connectionUtils.httpPost(url.toString(), httpHeaders, gson.toJson(createVaRequestDto));

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
}
