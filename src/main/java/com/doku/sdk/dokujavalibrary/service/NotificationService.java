package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationResponseBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    public PaymentNotificationResponseDto generateNotificationResponse(PaymentNotificationRequestBodyDto paymentNotificationRequestBodyDto) {
        return PaymentNotificationResponseDto.builder()
                .body(PaymentNotificationResponseBodyDto.builder()
                        .responseCode("2002500")
                        .responseMessage("Success")
                        .virtualAccountData(PaymentNotificationResponseBodyDto.PaymentNotificationResponseVirtualAccountDataDto.builder()
                                .partnerServiceId(paymentNotificationRequestBodyDto.getPartnerServiceId())
                                .customerNo(paymentNotificationRequestBodyDto.getCustomerNo())
                                .virtualAccountNo(paymentNotificationRequestBodyDto.getVirtualAccountNo())
                                .virtualAccountName(paymentNotificationRequestBodyDto.getVirtualAccountName())
                                .virtualAccountEmail(paymentNotificationRequestBodyDto.getVirtualAccountEmail())
                                .paymentRequestId(paymentNotificationRequestBodyDto.getPaymentRequestId())
                                .paidAmount(TotalAmountDto.builder()
                                        .value(paymentNotificationRequestBodyDto.getPaidAmount().getValue())
                                        .currency(paymentNotificationRequestBodyDto.getPaidAmount().getCurrency())
                                        .build())
                                .virtualAccountTrxType(paymentNotificationRequestBodyDto.getVirtualAccountTrxType())
                                .additionalInfo(AdditionalInfoDto.builder()
                                        .channel(paymentNotificationRequestBodyDto.getAdditionalInfo().getChannel())
                                        .virtualAccountConfig(VirtualAccountConfigDto.builder()
                                                .minAmount(paymentNotificationRequestBodyDto.getAdditionalInfo().getVirtualAccountConfig().getMinAmount())
                                                .maxAmount(paymentNotificationRequestBodyDto.getAdditionalInfo().getVirtualAccountConfig().getMaxAmount())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }

    public PaymentNotificationResponseDto generateInvalidTokenNotificationResponse() {
        return PaymentNotificationResponseDto.builder()
                .body(PaymentNotificationResponseBodyDto.builder()
                        .responseCode("4012701")
                        .responseMessage("Invalid Token (B2B)")
                        .build())
                .build();
    }
}
