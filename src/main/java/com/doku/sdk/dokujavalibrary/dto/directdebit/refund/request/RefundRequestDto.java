package com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDto {

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 12, groups = SizeValidation.class)
    private String originalPartnerReferenceNo;

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 36, groups = SizeValidation.class)
    private String originalExternalId;

    @Valid
    private TotalAmountDto refundAmount;

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 255, groups = SizeValidation.class)
    private String reason;

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 12, groups = SizeValidation.class)
    private String partnerRefundNo;

    @Valid
    private RefundAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;
    }

    public void validateRefundRequest(RefundRequestDto refundRequestDto) {
        if (!isValidChannel(refundRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("4005801", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
