package com.doku.sdk.dokujavalibrary.dto.va.inquiry.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestBodyDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String channelCode;
    private String trxDateInit;
    private String language;
    private String inquiryRequestId;
}
