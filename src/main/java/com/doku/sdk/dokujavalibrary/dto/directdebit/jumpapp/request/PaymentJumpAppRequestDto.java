package com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.doku.sdk.dokujavalibrary.validation.annotation.DateIso8601;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.PatternValidation;
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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentJumpAppRequestDto {
    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 64, groups = SizeValidation.class)
    private String partnerReferenceNo;

    @DateIso8601(groups = PatternValidation.class)
    private String validUpTo;

    @Size(max = 20, groups = SizeValidation.class)
    private String pointOfInitiation;

    @Valid
    private List<UrlParamDto> urlParam;

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

        private boolean supportDeepLinkCheckoutUrl;

        private CreateVaRequestDto.OriginDto origin;
    }

    public void validatePaymentJumpAppRequest(PaymentJumpAppRequestDto paymentJumpAppRequestDto) {
        if (!isValidChannel(paymentJumpAppRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("4005401", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

        if (!paymentJumpAppRequestDto.getPointOfInitiation().equalsIgnoreCase("app") &&
                !paymentJumpAppRequestDto.getPointOfInitiation().equalsIgnoreCase("pc") &&
                !paymentJumpAppRequestDto.getPointOfInitiation().equalsIgnoreCase("mweb")) {
            throw new GeneralException("4005401", "pointOfInitiation value can only be app/pc/mweb");
        }

        boolean isTypeValid = paymentJumpAppRequestDto.getUrlParam().stream()
                .anyMatch(class1 -> "PAY_RETURN".equalsIgnoreCase(class1.getType()));
        if(!isTypeValid) {
            throw new GeneralException("4005401", "urlParam.type must always be PAY_RETURN");
        }

    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
