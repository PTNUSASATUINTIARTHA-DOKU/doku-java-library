package com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request;

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
        private String payMethod;
        private TotalAmountDto transAmount;
        private TotalAmountDto feeAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAdditionalInfoRequestDto {
        private String channel;
        private String remarks; // allo, cimb
        private String successPaymentUrl;
        private String failedPaymentUrl;
        private List<LineItemsDto> lineItems; // allo
        private String paymentType; // bri, ovo
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineItemsDto {
        private String name;
        private String price;
        private String quantity;
    }

    public void validatePaymentRequest(PaymentRequestDto paymentRequestDto) {
        if (!isValidChannel(paymentRequestDto.getAdditionalInfo().getChannel())) {
            throw new BadRequestException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
