package com.doku.sdk.dokujavalibrary.dto.va;

import com.doku.au.security.module.constraint.SafeString;
import com.doku.sdk.dokujavalibrary.validation.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.SizeValidation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalInfoDto {

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class, message = "additionalInfo.channel must be a string. Ensure that additionalInfo.channel is enclosed in quotes. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    @Min(value = 1, groups = SizeValidation.class, message = "additionalInfo.channel must be at least 1 character long. Ensure that additionalInfo.channel is not empty. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    @Max(value = 30, groups = SizeValidation.class, message = "additionalInfo.channel must be 30 characters or fewer. Ensure that additionalInfo.channel is no longer than 30 characters. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    private String channel;

    private VirtualAccountConfigDto virtualAccountConfig;
    private String howToPayPage;
    private String howToPayApi;
}