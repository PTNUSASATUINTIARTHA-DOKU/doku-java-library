package com.doku.sdk.dokujavalibrary.dto.va.createva.response;

import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountDataDto;
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
public class CreateVaResponseDto {
    private String responseCode;
    private String responseMessage;
    private VirtualAccountDataDto virtualAccountData;
}
