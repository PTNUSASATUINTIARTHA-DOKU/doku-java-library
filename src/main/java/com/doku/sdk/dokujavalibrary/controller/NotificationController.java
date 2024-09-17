package com.doku.sdk.dokujavalibrary.controller;

import com.doku.sdk.dokujavalibrary.dto.directdebit.notification.request.DirectDebitNotificationRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.notification.response.DirectDebitNotificationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationResponseDto;
import com.doku.sdk.dokujavalibrary.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationController {

    private final NotificationService notificationService;

    public PaymentNotificationResponseDto generateNotificationResponse(PaymentNotificationRequestBodyDto paymentNotificationRequestBodyDto) {
        return notificationService.generateNotificationResponse(paymentNotificationRequestBodyDto);
    }

    public PaymentNotificationResponseDto generateInvalidTokenResponse() {
        return notificationService.generateInvalidTokenNotificationResponse();
    }

    public DirectDebitNotificationResponseDto generateDirectDebitNotificationResponse(DirectDebitNotificationRequestDto directDebitNotificationRequestDto) {
        return notificationService.generateDirectDebitNotificationResponse(directDebitNotificationRequestDto);
    }

    public DirectDebitNotificationResponseDto generateDirectDebitInvalidTokenResponse() {
        return notificationService.generateDirectDebitInvalidTokenNotificationResponse();
    }
}
