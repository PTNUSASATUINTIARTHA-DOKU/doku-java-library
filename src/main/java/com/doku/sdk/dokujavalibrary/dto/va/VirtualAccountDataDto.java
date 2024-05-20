package com.doku.sdk.dokujavalibrary.dto.va;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAccountDataDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String virtualAccountName;
    private String virtualAccountEmail;
    private String trxId;
    private TotalAmountDto totalAmount;
    private AdditionalInfoDto additionalInfo;
}
