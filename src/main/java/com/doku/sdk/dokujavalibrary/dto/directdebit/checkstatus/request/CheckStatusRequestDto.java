package com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.request;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckStatusRequestDto {

    @SafeString(groups = SafeStringValidation.class)
    private String originalPartnerReferenceNo;

    @SafeString(groups = SafeStringValidation.class)
    private String originalReferenceNo;

    @SafeString(groups = SafeStringValidation.class)
    private String originalExternalId;

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    private String serviceCode;

    @SafeString(groups = SafeStringValidation.class)
    private String transactionDate;

    @Valid
    private TotalAmountDto amount;

    @SafeString(groups = SafeStringValidation.class)
    private String merchantId;

    @SafeString(groups = SafeStringValidation.class)
    private String subMerchantId;

    @SafeString(groups = SafeStringValidation.class)
    private String externalStoreId;

    @Valid
    private CheckStatusAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckStatusAdditionalInfoRequestDto {
        @SafeString(groups = SafeStringValidation.class)
        private String deviceId;

        @SafeString(groups = SafeStringValidation.class)
        private String channel;

        private CreateVaRequestDto.OriginDto origin;
    }

    public void validateCheckStatusRequest(CheckStatusRequestDto checkStatusRequestDto) {
        if (checkStatusRequestDto.getServiceCode() == null || !checkStatusRequestDto.getServiceCode().equals("55")) {
            throw new GeneralException("5005500", "serviceCode must be '55'");
        }
    
        if (!isValidChannel(checkStatusRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("5005500", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
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


        @SafeString(groups = SafeStringValidation.class, message = "totalAmount.value must be a string. Ensure that totalAmount.value is enclosed in quotes. Example: '11500.00'.")
        @Size(min = 4, groups = SizeValidation.class, message = "totalAmount.value must be at least 4 characters long and formatted as 0.00. Ensure that totalAmount.value is at least 4 characters long and in the correct format. Example: '100.00'.")
        @Size(max = 19, groups = SizeValidation.class, message = "totalAmount.value must be 19 characters or fewer and formatted as 9999999999999999.99. Ensure that totalAmount.value is no longer than 19 characters and in the correct format. Example: '9999999999999999.99'.")
        @DecimalMin(value = "1", groups = SizeValidation.class)
        @Pattern(regexp = "[0-9]{1,16}\\.[0-9]{2}", groups = PatternValidation.class, message = "invalid pattern")
        private String value;


        @SafeString(groups = SafeStringValidation.class, message = "totalAmount.currency must be a string. Ensure that totalAmount.currency is enclosed in quotes. Example: 'IDR'.")
        private String currency;
    }
}
