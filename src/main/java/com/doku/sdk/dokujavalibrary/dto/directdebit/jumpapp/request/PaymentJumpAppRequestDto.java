package com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
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
public class PaymentJumpAppRequestDto {
    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 64, groups = SizeValidation.class)
    private String partnerReferenceNo;

    private String validUpTo;

    @Size(max = 20, groups = SizeValidation.class)
    private String pointOfInitiation;

    @Valid
    private UrlParamDto urlParam;

    @Valid
    private TotalAmountDto amount;

    @Valid
    private PaymentJumpAppAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UrlParamDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 255, groups = SizeValidation.class)
        private String url;

        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String type;

        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String isDeepLink;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentJumpAppAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;

        @SafeString(groups = SafeStringValidation.class)
        private String orderTitle; // dana

        @SafeString(groups = SafeStringValidation.class)
        private String metadata; // shopee pay
    }

    public void validatePaymentJumpAppRequest(PaymentJumpAppRequestDto paymentJumpAppRequestDto) {
        if (!isValidChannel(paymentJumpAppRequestDto.getAdditionalInfo().getChannel())) {
            throw new BadRequestException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
