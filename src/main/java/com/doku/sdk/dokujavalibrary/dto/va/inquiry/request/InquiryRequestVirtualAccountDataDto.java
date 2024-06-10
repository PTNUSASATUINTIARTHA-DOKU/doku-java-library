package com.doku.sdk.dokujavalibrary.dto.va.inquiry.request;

import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestVirtualAccountDataDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String virtualAccountName;
    private String virtualAccountEmail;
    private String virtualAccountPhone;
    private TotalAmountDto totalAmount;
    private String virtualAccountTrxType;
    private String expiredDate;
    private AdditionalInfoDto additionalInfo;
    private String inquiryStatus;
    private InquiryReasonDto inquiryReason;
    private String inquiryRequestId;
}
