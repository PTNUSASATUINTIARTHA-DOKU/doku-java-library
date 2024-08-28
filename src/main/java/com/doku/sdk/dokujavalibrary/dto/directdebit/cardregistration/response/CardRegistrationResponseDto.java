package com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.response;

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
public class CardRegistrationResponseDto {
    private String responseCode;
    private String responseMessage;
    private String referenceNo;
    private String redirectUrl;
    private CardRegistrationAdditionalInfoResponseDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CardRegistrationAdditionalInfoResponseDto {
        private String custIdMerchant;
        private String status;
        private String authCode;
    }
}
