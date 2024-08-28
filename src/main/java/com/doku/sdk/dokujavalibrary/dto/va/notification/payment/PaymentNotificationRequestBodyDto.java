package com.doku.sdk.dokujavalibrary.dto.va.notification.payment;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationRequestBodyDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String virtualAccountName;
    private String trxId;
    private String paymentRequestId;
    private TotalAmountDto paidAmount;
    private String virtualAccountEmail;
    private String virtualAccountPhone;
}
