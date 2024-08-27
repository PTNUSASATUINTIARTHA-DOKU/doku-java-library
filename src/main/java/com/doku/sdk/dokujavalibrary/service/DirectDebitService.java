package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response.AccountBindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.request.AccountUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.response.AccountUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.request.BalanceInquiryRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.response.BalanceInquiryResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request.CardRegistrationRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.response.CardRegistrationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request.PaymentJumpAppRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response.PaymentJumpAppResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request.PaymentRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.response.PaymentResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request.RefundRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.response.RefundResponseDto;
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

    public CardRegistrationResponseDto doCardRegistrationProcess(RequestHeaderDto requestHeaderDto, CardRegistrationRequestDto cardRegistrationRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitCardRegistrationUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(cardRegistrationRequestDto));

        return gson.fromJson(response.getBody(), CardRegistrationResponseDto.class);
    }

    public PaymentResponseDto doPaymentProcess(RequestHeaderDto requestHeaderDto, PaymentRequestDto paymentRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.X_IP_ADDRESS, requestHeaderDto.getXIpAddress());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER_CUSTOMER, requestHeaderDto.getAuthorizationCustomer());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitPaymentUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(paymentRequestDto));

        return gson.fromJson(response.getBody(), PaymentResponseDto.class);
    }

    public PaymentJumpAppResponseDto doPaymentJumpAppProcess(RequestHeaderDto requestHeaderDto, PaymentJumpAppRequestDto paymentJumpAppRequestDto, boolean isProduction) {
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

        String url = SdkConfig.getDirectDebitPaymentUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(paymentJumpAppRequestDto));

        return gson.fromJson(response.getBody(), PaymentJumpAppResponseDto.class);
    }

    public RefundResponseDto doRefundProcess(RequestHeaderDto requestHeaderDto, RefundRequestDto refundRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.X_IP_ADDRESS, requestHeaderDto.getXIpAddress());
        httpHeaders.set(SnapHeaderConstant.BEARER_CUSTOMER, requestHeaderDto.getAuthorizationCustomer());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitRefundUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(refundRequestDto));

        return gson.fromJson(response.getBody(), RefundResponseDto.class);
    }

    public BalanceInquiryResponseDto doBalanceInquiryProcess(RequestHeaderDto requestHeaderDto, BalanceInquiryRequestDto balanceInquiryRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.X_IP_ADDRESS, requestHeaderDto.getXIpAddress());
        httpHeaders.set(SnapHeaderConstant.BEARER_CUSTOMER, requestHeaderDto.getAuthorizationCustomer());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitBalanceInquiryUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(balanceInquiryRequestDto));

        return gson.fromJson(response.getBody(), BalanceInquiryResponseDto.class);
    }
}
