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
    @DateIso8601(groups = PatternValidation.class)
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
            throw new GeneralException("4005502", "serviceCode must be '55'");
        }
    
        if (!isValidChannel(checkStatusRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("4005501", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }
    }
    

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
