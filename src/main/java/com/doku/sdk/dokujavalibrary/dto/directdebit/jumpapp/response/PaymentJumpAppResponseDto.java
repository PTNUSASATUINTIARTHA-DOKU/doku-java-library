package com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response;

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
public class PaymentJumpAppResponseDto {
    private String responseCode;
    private String responseMessage;
    private String webRedirectUrl;
    private String partnerReferenceNo;
}
