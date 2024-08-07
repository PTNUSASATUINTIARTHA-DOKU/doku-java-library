package com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.response;

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
public class AccountUnbindingResponseDto {
    private String responseCode;
    private String responseMessage;
    private String referenceNo;
}
