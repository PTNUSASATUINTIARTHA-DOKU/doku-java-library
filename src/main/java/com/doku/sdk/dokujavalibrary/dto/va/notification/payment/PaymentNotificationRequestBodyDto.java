package com.doku.sdk.dokujavalibrary.dto.va.notification.payment;

import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
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
    private String virtualAccountEmail;
    private String trxId;
    private String paymentRequestId;
    private TotalAmountDto paidAmount;
    private String virtualAccountPhone;
    private AdditionalInfoDto additionalInfo;
    private String trxDateTime;
    private String virtualAccountTrxType;
}
