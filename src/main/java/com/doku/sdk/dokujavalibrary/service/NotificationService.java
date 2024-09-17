package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.dto.directdebit.notification.request.DirectDebitNotificationRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.notification.response.DirectDebitNotificationResponseDto;
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
                                .trxId(paymentNotificationRequestBodyDto.getTrxId())
                                .paymentRequestId(paymentNotificationRequestBodyDto.getPaymentRequestId())
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

    public DirectDebitNotificationResponseDto generateDirectDebitNotificationResponse(DirectDebitNotificationRequestDto directDebitNotificationRequestDto) {
        return DirectDebitNotificationResponseDto.builder()
                .responseCode("2005600")
                .approvalCode("201039000200") // tba
                .responseMessage("Request has been processed successfully")
                .build();
    }

    public DirectDebitNotificationResponseDto generateDirectDebitInvalidTokenNotificationResponse() {
        return DirectDebitNotificationResponseDto.builder()
                .responseCode("5005600")
                .responseMessage("Invalid Token")
                .build();
    }
}
