package com.doku.sdk.dokujavalibrary.dto.va.notification.payment;

import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
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
    private PaymentNotificationRequestAdditionalInfoDto additionalInfo;
    private String trxDateTime;
    private String virtualAccountTrxType;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentNotificationRequestAdditionalInfoDto {
        private String channel;
        private VirtualAccountConfigDto virtualAccountConfig;
        private String senderName;
        private String sourceAccountNo;
        private String sourceBankCode;
        private String sourceBankName;
    }
}
