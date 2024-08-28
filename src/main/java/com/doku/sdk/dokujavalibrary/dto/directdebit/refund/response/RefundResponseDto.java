package com.doku.sdk.dokujavalibrary.dto.directdebit.refund.response;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundResponseDto {
    private String responseCode;
    private String responseMessage;
    private TotalAmountDto refundAmount;
    private String originalPartnerReferenceNo;
    private String originalReferenceNo;
    private String refundNo;
    private String partnerRefundNo;
    private String refundTime;
}
