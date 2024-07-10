package com.doku.sdk.dokujavalibrary.dto.va.inquiry.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseBodyDto {
    private String responseCode;
    private String responseMessage;
    private InquiryResponseVirtualAccountDataDto virtualAccountData;
}
