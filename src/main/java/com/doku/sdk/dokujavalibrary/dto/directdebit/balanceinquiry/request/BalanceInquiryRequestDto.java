package com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.request;

import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceInquiryRequestDto {
    @Valid
    private BalanceInquiryAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceInquiryAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;

        private CreateVaRequestDto.OriginDto origin;
    }

    public void validateBalanceInquiryRequest(BalanceInquiryRequestDto balanceInquiryRequestDto) {
        if (!isValidChannel(balanceInquiryRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("4001101", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
