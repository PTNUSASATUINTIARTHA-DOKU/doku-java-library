package com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.response;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckStatusResponseDto {
    private String responseCode;
    private String responseMessage;
    private String originalPartnerReferenceNo;
    private String originalReferenceNo;
    private String approvalCode;
    private String originalExternalId;
    private String serviceCode;
    private String latestTransactionStatus;
    private String transactionStatusDesc;
    private String originalResponseCode;
    private String originalResponseMessage;
    private String sessionId;
    private String requestId;
    private List<RefundHistoryDto> refundHistory;
    private TotalAmountDto transAmount;
    private TotalAmountDto feeAmount;
    private String paidTime;
    private CheckStatusAdditionalInfoResponseDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RefundHistoryDto {
        private String refundNo;
        private String partnerReferenceNo;
        private TotalAmountDto refundAmount;
        private String refundStatus;
        private String refundDate;
        private String reason;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CheckStatusAdditionalInfoResponseDto {
        private String deviceId;
        private String channel;
        private Object acquirer;
    }
}
