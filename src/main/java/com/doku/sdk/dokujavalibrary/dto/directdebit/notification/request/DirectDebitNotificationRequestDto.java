package com.doku.sdk.dokujavalibrary.dto.directdebit.notification.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.LineItemsDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.OriginDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectDebitNotificationRequestDto {
    private DirectDebitNotificationHeaderRequestDto header;
    private DirectDebitNotificationBodyRequestDto body;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectDebitNotificationHeaderRequestDto {
        private String xTimestamp;
        private String xSignature;
        private String xPartnerId;
        private String xExternalId;
        private String xDeviceId;
        private String xIpAddress;
        private String authorizationCustomer;
        private String authorization;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectDebitNotificationBodyRequestDto {
        private String originalPartnerReferenceNo;
        private String originalReferenceNo;
        private String originalExternalId;
        private String latestTransactionStatus;
        private String transactionStatusDesc;
        private TotalAmountDto amount;
        private DirectDebitNotificationAdditionalInfoRequestDto additionalInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectDebitNotificationAdditionalInfoRequestDto {
        private String channel;
        private String acquirerId;
        private String custIdMerchant;
        private String accountType;
        private List<LineItemsDto> lineItems;
        private OriginDto origin;
    }
}
