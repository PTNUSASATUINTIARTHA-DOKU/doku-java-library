package com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    @Size(min = 32, groups = SizeValidation.class)
    @Size(max = 64, groups = SizeValidation.class)
    private String partnerReferenceNo;

    @SafeString(groups = SafeStringValidation.class)
    private String feeType; // ovo

    @Valid
    private TotalAmountDto amount;

    @Valid
    private List<PayOptionDetailsDto> payOptionDetails; // allo, ovo

    @Valid
    private PaymentAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayOptionDetailsDto {
        @SafeString(groups = SafeStringValidation.class)
        private String payMethod;

        @Valid
        private TotalAmountDto transAmount;

        @Valid
        private TotalAmountDto feeAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 40, groups = SizeValidation.class)
        private String remarks; // allo, cimb

        private String successPaymentUrl;

        private String failedPaymentUrl;

        @Valid
        private List<LineItemsDto> lineItems; // allo

        @SafeString(groups = SafeStringValidation.class)
        private String paymentType; // bri, ovo
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineItemsDto {
        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 32, groups = SizeValidation.class)
        private String name;
        @SafeString(groups = SafeStringValidation.class)
        @Pattern(regexp = "[0-9]{1,16}\\.[0-9]{2}", groups = PatternValidation.class, message = "invalid pattern")
        private String price;
        @SafeString(groups = SafeStringValidation.class)
        private String quantity;
    }

    public void validatePaymentRequest(PaymentRequestDto paymentRequestDto) {
        if (!isValidChannel(paymentRequestDto.getAdditionalInfo().getChannel())) {
            throw new BadRequestException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.EMONEY_OVO_SNAP.name())) {
            if (!paymentRequestDto.getFeeType().isEmpty()) {
                if (!paymentRequestDto.getFeeType().equalsIgnoreCase("OUR") ||
                        !paymentRequestDto.getFeeType().equalsIgnoreCase("BEN") ||
                        !paymentRequestDto.getFeeType().equalsIgnoreCase("SHA")) {
                    throw new BadRequestException("", "Value can only be OUR/BEN/SHA for EMONEY_OVO_SNAP");
                }
            }

            if (paymentRequestDto.getPayOptionDetails().isEmpty()) {
                throw new BadRequestException("", "Pay Option Details cannot be empty for EMONEY_OVO_SNAP");
            }

            if (!paymentRequestDto.getAdditionalInfo().getPaymentType().isEmpty()) {
                if (!paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("SALE") &&
                        !paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("RECURRING")) {
                    throw new BadRequestException("", "additionalInfo.paymentType cannot be empty for EMONEY_OVO_SNAP");
                }
            }
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            if (paymentRequestDto.getAdditionalInfo().getLineItems().isEmpty()) {
                throw new BadRequestException("", "additionalInfo.lineItems cannot be empty for DIRECT_DEBIT_ALLO_SNAP");
            }

            if (paymentRequestDto.getAdditionalInfo().getRemarks().isEmpty()) {
                throw new BadRequestException("", "additionalInfo.remarks cannot be empty for DIRECT_DEBIT_ALLO_SNAP");
            }
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_CIMB_SNAP.name())) {
            if (paymentRequestDto.getAdditionalInfo().getRemarks().isEmpty()) {
                throw new BadRequestException("", "additionalInfo.remarks cannot be empty for DIRECT_DEBIT_CIMB_SNAP");
            }
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_BRI_SNAP.name())) {
            if (!paymentRequestDto.getAdditionalInfo().getPaymentType().isEmpty()) {
                if (!paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("SALE") &&
                        !paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("RECURRING")) {
                    throw new BadRequestException("", "additionalInfo.paymentType cannot be empty for DIRECT_DEBIT_BRI_SNAP");
                }
            }
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
