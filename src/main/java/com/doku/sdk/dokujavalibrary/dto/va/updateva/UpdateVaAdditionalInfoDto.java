package com.doku.sdk.dokujavalibrary.dto.va.updateva;

import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.AmountValidation;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVaAdditionalInfoDto {

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class, message = "additionalInfo.channel must be a string. Ensure that additionalInfo.channel is enclosed in quotes. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    @Size(min = 1, groups = SizeValidation.class, message = "additionalInfo.channel must be at least 1 character long. Ensure that additionalInfo.channel is not empty. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    @Size(max = 30, groups = SizeValidation.class, message = "additionalInfo.channel must be 30 characters or fewer. Ensure that additionalInfo.channel is no longer than 30 characters. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    private String channel;

    @Valid
    private UpdateVaVirtualAccountConfigDto virtualAccountConfig;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateVaVirtualAccountConfigDto {

        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class, message = "status must be a string. Ensure that status is enclosed in quotes. Example: ‘INACTIVE’.")
        @Size(min = 1, groups = SizeValidation.class, message = "status must be at least 1 character long. Ensure that status is not empty. Example: ‘INACTIVE’.")
        @Size(max = 20, groups = SizeValidation.class, message = "status must be 20 characters or fewer. Ensure that status is no longer than 20 characters. Example: ‘INACTIVE’.")
        private String status;

        @Digits(integer = 16, fraction = 2, groups = AmountValidation.class)
        @DecimalMin(value = "1", groups = SizeValidation.class)
        private BigDecimal minAmount;

        @Digits(integer = 16, fraction = 2, groups = AmountValidation.class)
        private BigDecimal maxAmount;
    }
}
