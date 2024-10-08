package com.doku.sdk.dokujavalibrary.dto.va.checkstatusva;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.BillAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusResponsePaymentFlagReasonDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckStatusVirtualAccountDataDto {
    private CheckStatusResponsePaymentFlagReasonDto paymentFlagReason;
    private String partnerServiceId;
    private String customerNo;
    private String virtualAccountNo;
    private String inquiryRequestId;
    private String paymentRequestId;
    private String trxId;
    private TotalAmountDto paidAmount;
    private List<BillAmountDto> billDetails;
}
