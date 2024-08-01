package com.doku.sdk.dokujavalibrary.dto.va.inquiry.response;

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
public class InquiryResponseBodyDto {
    private String responseCode;
    private String responseMessage;
    private InquiryResponseVirtualAccountDataDto virtualAccountData;
}
