package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response.AccountBindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.request.AccountUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.response.AccountUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DirectDebitService {

    private final ConnectionUtils connectionUtils;
    private final Gson gson;

    public AccountBindingResponseDto doAccountBindingProcess(RequestHeaderDto requestHeaderDto, AccountBindingRequestDto accountBindingRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.X_DEVICE_ID, requestHeaderDto.getXDeviceId());
        httpHeaders.set(SnapHeaderConstant.X_IP_ADDRESS, requestHeaderDto.getXIpAddress());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitAccountBindingUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(accountBindingRequestDto));

        return gson.fromJson(response.getBody(), AccountBindingResponseDto.class);
    }

    public AccountUnbindingResponseDto doAccountUnbindingProcess(RequestHeaderDto requestHeaderDto, AccountUnbindingRequestDto accountUnbindingRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.X_IP_ADDRESS, requestHeaderDto.getXIpAddress());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitAccountUnbindingUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(accountUnbindingRequestDto));

        return gson.fromJson(response.getBody(), AccountUnbindingResponseDto.class);
    }
}
