package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.common.SnapUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response.AccountBindingResponseDto;
import com.doku.sdk.dokujavalibrary.service.DirectDebitService;
import com.doku.sdk.dokujavalibrary.service.TokenService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectDebitController {

    private final DirectDebitService directDebitService;
    private final TokenService tokenService;
    private final SnapUtils snapUtils;
    private final Gson gson;

    public AccountBindingResponseDto doAccountBinding(AccountBindingRequestDto accountBindingRequestDto,
                                                      String secretKey,
                                                      String clientId,
                                                      String deviceId,
                                                      String ipAddress,
                                                      String tokenB2b,
                                                      boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitAccountBindingUrl(isProduction);
        String requestBody = gson.toJson(accountBindingRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.PUT.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, deviceId, ipAddress, null, tokenB2b);

        return directDebitService.doAccountBindingProcess(requestHeader, accountBindingRequestDto, isProduction);
    }
}
