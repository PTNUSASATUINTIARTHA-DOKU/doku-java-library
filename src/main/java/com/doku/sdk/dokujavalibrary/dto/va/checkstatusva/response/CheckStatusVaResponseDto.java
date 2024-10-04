package com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response;

import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.CheckStatusVirtualAccountDataDto;
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
public class CheckStatusVaResponseDto {
    private String responseCode;
    private String responseMessage;
    private CheckStatusVirtualAccountDataDto virtualAccountData;
    private CheckStatusResponseAdditionalInfoDto additionalInfo;
}
