package com.doku.sdk.dokujavalibrary.dto.va.notification.payment;

import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
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
    private PaymentNotificationResponseVirtualAccountDataDto virtualAccountData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentNotificationResponseVirtualAccountDataDto {
        private String partnerServiceId;
        private String customerNo;
        private String virtualAccountNo;
        private String virtualAccountName;
        private String virtualAccountEmail;
        private String paymentRequestId;
        private TotalAmountDto paidAmount;
        private String virtualAccountTrxType;
        private AdditionalInfoDto additionalInfo;
    }
}
