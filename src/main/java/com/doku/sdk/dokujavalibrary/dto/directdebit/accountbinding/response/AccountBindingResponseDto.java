package com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBindingResponseDto {
    private String responseCode;
    private String responseMessage;
    private String referenceNo;
    private String redirectUrl;
    private AccountBindingAdditionalInfoResponseDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBindingAdditionalInfoResponseDto {
        private String custIdMerchant;
        private String status;
        private String authCode;
    }
}
