package com.doku.sdk.dokujavalibrary.dto.response;

import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVaResponseDto {
    private String responseCode;
    private String responseMessage;
    private VirtualAccountDataDto virtualAccountData;
}
