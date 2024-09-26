package com.doku.sdk.dokujavalibrary.dto.token.request;

import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
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
    private String grantType;
}
