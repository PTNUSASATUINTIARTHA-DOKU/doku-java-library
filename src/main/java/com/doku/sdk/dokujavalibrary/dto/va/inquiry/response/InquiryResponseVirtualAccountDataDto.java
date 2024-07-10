package com.doku.sdk.dokujavalibrary.dto.va.inquiry.response;

import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.InquiryReasonDto;
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
public class InquiryResponseVirtualAccountDataDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String virtualAccountName;
    private String virtualAccountEmail;
    private String virtualAccountPhone;
    private TotalAmountDto totalAmount;
    private String virtualAccountTrxType;
    private String expiredDate;
    private InquiryResponseAdditionalInfoDto additionalInfo;
    private String inquiryStatus;
    private InquiryReasonDto inquiryReason;
    private String inquiryRequestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InquiryResponseAdditionalInfoDto {
        private String channel;
        private String trxId;
        private VirtualAccountConfigDto virtualAccountConfig;
    }
}
