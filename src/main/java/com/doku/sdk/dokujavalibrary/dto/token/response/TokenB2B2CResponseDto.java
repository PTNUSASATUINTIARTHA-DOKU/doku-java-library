package com.doku.sdk.dokujavalibrary.dto.token.response;

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
public class TokenB2B2CResponseDto {
    private String responseCode;
    private String responseMessage;
    private String accessToken;
    private String tokenType;
    private String accessTokenExpiryTime;
    private String refreshToken;
    private String refreshTokenExpiryTime;
    private Object additionalInfo;
}
