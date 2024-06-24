package com.doku.sdk.dokujavalibrary.dto.va.deleteva.response;

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
public class DeleteVaResponseVirtualAccountDataDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String trxId;
    private DeleteVaResponseAdditionalInfoDto additionalInfo;
}
