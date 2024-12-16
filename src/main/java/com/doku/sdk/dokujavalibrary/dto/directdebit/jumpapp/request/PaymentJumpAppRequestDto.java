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
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
        if(paymentJumpAppRequestDto.amount == null){
            throw new GeneralException("4005402", "amount value can not be null");
        }

        if(paymentJumpAppRequestDto.amount.getValue() == null ||
        paymentJumpAppRequestDto.amount.getValue().isEmpty()){
            throw new GeneralException("4005402", "Invalid Field Format amount.value");
        }

        if(paymentJumpAppRequestDto.amount.getValue().length() < 4){
            throw new GeneralException("4005402", "totalAmount.value must be at least 4 characters long and formatted as 0.00. Ensure that totalAmount.value is at least 4 characters long and in the correct format. Example: '100.00'.");
        }

        if(paymentJumpAppRequestDto.amount.getValue().length() > 19){
            throw new GeneralException("4005402", "totalAmount.value must be 19 characters or fewer and formatted as 9999999999999999.99. Ensure that totalAmount.value is no longer than 19 characters and in the correct format. Example: '9999999999999999.99'.");
        }

        String amountPattern = "[0-9]{1,16}\\.[0-9]{2}";
        if(!paymentJumpAppRequestDto.amount.getValue().matches(amountPattern)){
            throw new GeneralException("4005402", "totalAmount.value invalid pattern.");
        }

        if (!isValidChannel(paymentJumpAppRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("5005400", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

        if(paymentJumpAppRequestDto.getPointOfInitiation() == null){
            throw new GeneralException("4095401", "pointOfInitiation value can only be app/pc/mweb");
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TotalAmountDto {

        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class, message = "totalAmount.value must be a string. Ensure that totalAmount.value is enclosed in quotes. Example: '11500.00'.")
        @Size(min = 4, groups = SizeValidation.class, message = "totalAmount.value must be at least 4 characters long and formatted as 0.00. Ensure that totalAmount.value is at least 4 characters long and in the correct format. Example: '100.00'.")
        @Size(max = 19, groups = SizeValidation.class, message = "totalAmount.value must be 19 characters or fewer and formatted as 9999999999999999.99. Ensure that totalAmount.value is no longer than 19 characters and in the correct format. Example: '9999999999999999.99'.")
        @DecimalMin(value = "1", groups = SizeValidation.class)
        @Pattern(regexp = "[0-9]{1,16}\\.[0-9]{2}", groups = PatternValidation.class, message = "invalid pattern")
        private String value;

        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class, message = "totalAmount.currency must be a string. Ensure that totalAmount.currency is enclosed in quotes. Example: 'IDR'.")
        @Pattern(regexp = "^[a-zA-Z]{3}$", groups = PatternValidation.class, message = "totalAmount.currency must be exactly 3 characters long. Ensure that totalAmount.currency is exactly 3 characters. Example: 'IDR'.")
        private String currency;
    }
}
