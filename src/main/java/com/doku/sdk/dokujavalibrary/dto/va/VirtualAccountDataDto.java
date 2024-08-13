package com.doku.sdk.dokujavalibrary.dto.va;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
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
public class VirtualAccountDataDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String virtualAccountName;
    private String virtualAccountEmail;
    private String trxId;
    private TotalAmountDto totalAmount;
    private String virtualAccountTrxType;
    private String expiredDate;
    private AdditionalInfoDto additionalInfo;
}
