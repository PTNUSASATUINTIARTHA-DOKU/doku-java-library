package com.doku.sdk.dokujavalibrary.dto.va.notification.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationResponseHeaderDto {
    private String xTimestamp;
    private String contentType;
}
