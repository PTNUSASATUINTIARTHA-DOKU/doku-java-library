package com.doku.sdk.dokujavalibrary.dto.va.deleteva.request;

import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
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
public class DeleteVaRequestAdditionalInfoDto {

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class, message = "additionalInfo.channel must be a string. Ensure that additionalInfo.channel is enclosed in quotes. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    @Min(value = 1, groups = SizeValidation.class, message = "additionalInfo.channel must be at least 1 character long. Ensure that additionalInfo.channel is not empty. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    @Max(value = 30, groups = SizeValidation.class, message = "additionalInfo.channel must be 30 characters or fewer. Ensure that additionalInfo.channel is no longer than 30 characters. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.")
    private String channel;
}
