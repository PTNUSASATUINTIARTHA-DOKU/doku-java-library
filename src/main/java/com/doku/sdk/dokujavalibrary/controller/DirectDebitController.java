package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.common.SnapUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response.AccountBindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.request.AccountUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.response.AccountUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.request.BalanceInquiryRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.response.BalanceInquiryResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request.CardRegistrationRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.response.CardRegistrationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.request.CardUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.response.CardUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.request.CheckStatusRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.response.CheckStatusResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request.PaymentJumpAppRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response.PaymentJumpAppResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request.PaymentRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.response.PaymentResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request.RefundRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.response.RefundResponseDto;
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
                                                      Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitAccountBindingUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(accountBindingRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, deviceId, ipAddress, null, null, tokenB2b);

        return directDebitService.doAccountBindingProcess(requestHeader, accountBindingRequestDto, isProduction);
    }

    public AccountUnbindingResponseDto doAccountUnbinding(AccountUnbindingRequestDto accountUnbindingRequestDto,
                                                          String secretKey,
                                                          String clientId,
                                                          String ipAddress,
                                                          String tokenB2b,
                                                          Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitAccountUnbindingUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(accountUnbindingRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, ipAddress, null, null, tokenB2b);

        return directDebitService.doAccountUnbindingProcess(requestHeader, accountUnbindingRequestDto, isProduction);
    }

    public CardRegistrationResponseDto doCardRegistration(CardRegistrationRequestDto cardRegistrationRequestDto,
                                                          String secretKey,
                                                          String clientId,
                                                          String channelId,
                                                          String tokenB2b,
                                                          Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitCardRegistrationUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(cardRegistrationRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null, channelId, null, tokenB2b);

        return directDebitService.doCardRegistrationProcess(requestHeader, cardRegistrationRequestDto, isProduction);
    }

    public CardUnbindingResponseDto doCardUnbinding(CardUnbindingRequestDto cardUnbindingRequestDto,
                                                    String secretKey,
                                                    String clientId,
                                                    String tokenB2b,
                                                    Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitCardUnbindingUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(cardUnbindingRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null, null, null, tokenB2b);

        return directDebitService.doCardUnbindingProcess(requestHeader, cardUnbindingRequestDto, isProduction);
    }

    public PaymentResponseDto doPayment(PaymentRequestDto paymentRequestDto,
                                        String secretKey,
                                        String clientId,
                                        String ipAddress,
                                        String channelId,
                                        String tokenB2b2c,
                                        String tokenB2b,
                                        Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitPaymentUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(paymentRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, ipAddress, channelId, tokenB2b2c, tokenB2b);

        return directDebitService.doPaymentProcess(requestHeader, paymentRequestDto, isProduction);
    }

    public PaymentJumpAppResponseDto doPaymentJumpApp(PaymentJumpAppRequestDto paymentJumpAppRequestDto,
                                                      String secretKey,
                                                      String clientId,
                                                      String deviceId,
                                                      String ipAddress,
                                                      String tokenB2b,
                                                      Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitPaymentUrl(isProduction).replace(SdkConfig.getBaseUrl(isProduction), "");
        String requestBody = gson.toJson(paymentJumpAppRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, deviceId, ipAddress, null, null, tokenB2b);

        return directDebitService.doPaymentJumpAppProcess(requestHeader, paymentJumpAppRequestDto, isProduction);
    }

    public RefundResponseDto doRefund(RefundRequestDto refundRequestDto,
                                      String secretKey,
                                      String clientId,
                                      String ipAddress,
                                      String tokenB2b,
                                      String tokenB2b2c,
                                      Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitRefundUrl(isProduction);
        String requestBody = gson.toJson(refundRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, ipAddress, null, tokenB2b2c, tokenB2b);

        return directDebitService.doRefundProcess(requestHeader, refundRequestDto, isProduction);
    }

    public BalanceInquiryResponseDto doBalanceInquiry(BalanceInquiryRequestDto balanceInquiryRequestDto,
                                                      String secretKey,
                                                      String clientId,
                                                      String ipAddress,
                                                      String tokenB2b,
                                                      String tokenB2b2c,
                                                      Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitBalanceInquiryUrl(isProduction);
        String requestBody = gson.toJson(balanceInquiryRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, ipAddress, null, tokenB2b2c, tokenB2b);

        return directDebitService.doBalanceInquiryProcess(requestHeader, balanceInquiryRequestDto, isProduction);
    }

    public CheckStatusResponseDto doCheckStatus(CheckStatusRequestDto checkStatusRequestDto,
                                                String secretKey,
                                                String clientId,
                                                String tokenB2b,
                                                Boolean isProduction) {
        String endpointUrl = SdkConfig.getDirectDebitCheckStatusUrl(isProduction);
        String requestBody = gson.toJson(checkStatusRequestDto);

        String timestamp = tokenService.getTimestamp();
        String signature = tokenService.generateSymmetricSignature(HttpMethod.POST.name(), endpointUrl, tokenB2b, requestBody, timestamp, secretKey);
        String externalId = snapUtils.generateExternalId();

        var requestHeader = snapUtils.generateRequestHeaderDto(timestamp, signature, clientId, externalId, null, null, null, null, tokenB2b);

        return directDebitService.doCheckStatusProcess(requestHeader, checkStatusRequestDto, isProduction);
    }
}
