package com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.response;

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
public class CardUnbindingResponseDto {
    private String responseCode;
    private String responseMessage;
    private String referenceNo;
    private String redirectUrl;
}
