package com.doku.sdk.dokujavalibrary.dto.va.notification.payment;

import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationResponseBodyDto {
    private String responseCode;
    private String responseMessage;
    private VirtualAccountDataDto virtualAccountData;
}
