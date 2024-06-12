package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.common.ValidationUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(VaService.class);
    private final TokenService tokenService;
    private final ConnectionUtils connectionUtils;
    private final Gson gson;

    public String generateExternalId() {
        UUID uuid = UUID.randomUUID();
        long timestamp = Instant.now().getEpochSecond();

        return uuid.toString() + timestamp;
    }

    public RequestHeaderDto generateRequestHeaderDto(String channelId, String clientId, String tokenB2b, PrivateKey privateKey) {
        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.createSignature(privateKey, clientId, timestamp);

        return RequestHeaderDto.builder()
                .xTimestamp(timestamp)
                .xSignature(signature)
                .xPartnerId(clientId)
                .xExternalId(generateExternalId())
                .channelId(channelId)
                .authorization(tokenB2b)
                .build();
    }

    public CreateVaResponseDto createVa(RequestHeaderDto requestHeaderDto, CreateVaRequestDto createVaRequestDto, Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(createVaRequestDto);
        } catch (BadRequestException e) {
            return CreateVaResponseDto.builder()
                    .responseCode(e.getResponseCode())
                    .responseMessage(e.getMessage())
                    .build();
        }

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
}
