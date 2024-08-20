package com.doku.sdk.dokujavalibrary.dto.token.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenB2B2CRequestDto {
    private String grantType;
    private String authCode;
    private String refreshToken;
    private Object AdditionalInfo;
}
