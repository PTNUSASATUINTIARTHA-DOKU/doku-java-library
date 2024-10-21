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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DirectDebitService {

    private static final Log log = LogFactory.getLog(DirectDebitService.class);
    private final ConnectionUtils connectionUtils;
    private final Gson gson;

    public AccountBindingResponseDto doAccountBindingProcess(RequestHeaderDto requestHeaderDto, AccountBindingRequestDto accountBindingRequestDto, boolean isProduction) {
        requestHeaderDto.validateAccountBindingHeader(accountBindingRequestDto.getAdditionalInfo().getChannel());
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
        requestHeaderDto.validateAccountUnbindingHeader(accountUnbindingRequestDto.getAdditionalInfo().getChannel());
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
        Gson gsonSystem = new GsonBuilder().disableHtmlEscaping().create();
        var response = connectionUtils.httpPost(url, httpHeaders, gsonSystem.toJson(cardRegistrationRequestDto));
        return gson.fromJson(response.getBody(), CardRegistrationResponseDto.class);
    }

    public CardUnbindingResponseDto doCardUnbindingProcess(RequestHeaderDto requestHeaderDto, CardUnbindingRequestDto cardUnbindingRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitCardUnbindingUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(cardUnbindingRequestDto));

        return gson.fromJson(response.getBody(), CardUnbindingResponseDto.class);
    }

    public PaymentResponseDto doPaymentProcess(RequestHeaderDto requestHeaderDto, PaymentRequestDto paymentRequestDto, boolean isProduction) {
        requestHeaderDto.validatePaymentHeader(paymentRequestDto.getAdditionalInfo().getChannel());
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
        requestHeaderDto.validateRefundHeader(refundRequestDto.getAdditionalInfo().getChannel());
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
        requestHeaderDto.validateBalanceInquiryHeader(balanceInquiryRequestDto.getAdditionalInfo().getChannel());
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

    public CheckStatusResponseDto doCheckStatusProcess(RequestHeaderDto requestHeaderDto, CheckStatusRequestDto checkStatusRequestDto, boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDirectDebitCheckStatusUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(checkStatusRequestDto));

        return gson.fromJson(response.getBody(), CheckStatusResponseDto.class);
    }

    public String encryptCbc(String input, String secretKey) {
        String AES = "AES";
        String AES_CBC_PADDING = "AES/CBC/PKCS5Padding";
        try {
            secretKey = getSecretKey(secretKey);
            IvParameterSpec ivParameterSpec = generateIv();
            SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING); //NOSONAR
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            String chiperString = Base64.getEncoder().encodeToString(cipherText);
            String ivString = Base64.getEncoder().encodeToString(ivParameterSpec.getIV());
            return chiperString + "|" + ivString;
        } catch(GeneralSecurityException ex) {
            // throw error
        }
        return "";
    }

    private String getSecretKey(String secretKey) {
        if(secretKey.length() > 16){
            return secretKey.substring(0, 16);
        } else if(secretKey.length() < 16){
            return StringUtils.rightPad(secretKey, 16, '-');
        } else {
            return secretKey;
        }
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
