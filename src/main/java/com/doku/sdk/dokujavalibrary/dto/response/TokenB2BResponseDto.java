package com.doku.sdk.dokujavalibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenB2BResponseDto {

    private String responseCode;
    private String responseMessage;
    private String accessToken;
    private String tokenType;
    private String expiresIn;
    private Map<String, String> additionalInfo;
}
