package com.doku.sdk.dokujavalibrary.dto.va.inquiry.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.InquiryReasonDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.exception.SimulatorException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryRequestBodyDto {
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String channelCode;
    private String trxDateInit;
    private String language;
    private String inquiryRequestId;
    private InquiryRequestAdditionalInfoDto additionalInfo;

    public void validateInquiryVaSimulator(InquiryRequestBodyDto inquiryRequestBodyDto, Boolean isProduction) {
        if (!isProduction) {
            if (inquiryRequestBodyDto.getAdditionalInfo().getTrxId().startsWith("1117") || inquiryRequestBodyDto.getAdditionalInfo().getTrxId().startsWith("116")) {
                var object = InquiryResponseBodyDto.builder()
                        .responseCode("2002400")
                        .responseMessage("Success")
                        .virtualAccountData(InquiryResponseVirtualAccountDataDto.builder()
                                .partnerServiceId("   77062")
                                .customerNo("2007")
                                .virtualAccountNo("   7706220010")
                                .virtualAccountName("Test")
                                .virtualAccountEmail("test@email.com")
                                .virtualAccountPhone("628123456789")
                                .totalAmount(TotalAmountDto.builder()
                                        .value("25000.00")
                                        .currency("IDR")
                                        .build())
                                .virtualAccountTrxType("C")
                                .additionalInfo(InquiryResponseVirtualAccountDataDto.InquiryResponseAdditionalInfoDto.builder()
                                        .channel("VIRTUAL_ACCOUNT_BRI")
                                        .trxId("INV_20240913001")
                                        .virtualAccountConfig(VirtualAccountConfigDto.builder()
                                                .reusableStatus(true)
                                                .minAmount(new BigDecimal("10000"))
                                                .maxAmount(new BigDecimal("100000"))
                                                .build())
                                        .build())
                                .inquiryReason(InquiryReasonDto.builder()
                                        .english("Success")
                                        .indonesia("Sukses")
                                        .build())
                                .inquiryRequestId("DIPCVA003T240913101610069TxXxsQnHAYA")
                                .freeText(List.of("english: Free text", "indonesia: Text bebas"))
                                .build())
                        .build();

                throw new SimulatorException("2002400", "Success", object);
            } else if (inquiryRequestBodyDto.getAdditionalInfo().getTrxId().startsWith("117")) {
                throw new SimulatorException("4042414", "Bill has been paid", null);
            } else if (inquiryRequestBodyDto.getAdditionalInfo().getTrxId().startsWith("118")) {
                throw new SimulatorException("4042419", "Bill expired", null);
            } else if (inquiryRequestBodyDto.getAdditionalInfo().getTrxId().startsWith("119")) {
                throw new SimulatorException("4042412", "Bill not found", null);
            }
        }
    }
}
