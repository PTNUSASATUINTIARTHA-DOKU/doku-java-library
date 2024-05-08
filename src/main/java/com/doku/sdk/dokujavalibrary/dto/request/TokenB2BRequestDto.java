package com.doku.sdk.dokujavalibrary.dto.request;

import com.doku.au.security.module.constraint.SafeString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenB2BRequestDto {

    private String signature;

    @SafeString
    @NotEmpty
    private String timestamp;

    @SafeString
    @NotEmpty
    private String clientId;
    private String grantType;
}
