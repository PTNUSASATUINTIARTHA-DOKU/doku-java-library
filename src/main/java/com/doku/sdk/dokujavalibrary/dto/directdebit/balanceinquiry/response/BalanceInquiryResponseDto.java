package com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.response;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
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
public class BalanceInquiryResponseDto {
    private String responseCode;
    private String responseMessage;
    private List<AccountInfosDto> accountInfos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AccountInfosDto {
        private String balanceType;
        private TotalAmountDto amount;
        private TotalAmountDto flatAmount;
        private TotalAmountDto holdAmount;
    }
}
