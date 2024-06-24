package com.doku.sdk.dokujavalibrary.dto.va;

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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalAmountDto {

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class, message = "totalAmount.value must be a string. Ensure that totalAmount.value is enclosed in quotes. Example: '11500.00'.")
    @Size(min = 4, groups = SizeValidation.class, message = "totalAmount.value must be at least 4 characters long and formatted as 0.00. Ensure that totalAmount.value is at least 4 characters long and in the correct format. Example: '100.00'.")
    @Size(max = 19, groups = SizeValidation.class, message = "totalAmount.value must be 19 characters or fewer and formatted as 9999999999999999.99. Ensure that totalAmount.value is no longer than 19 characters and in the correct format. Example: '9999999999999999.99'.")
    @Pattern(regexp = "^(0|[1-9]\\d{0,15})(\\.\\d{2})?$", groups = PatternValidation.class)
    private String value;

    @NotNull(groups = MandatoryValidation.class, message = "totalAmount.currency cannot be null. Please provide a totalAmount.currency. Example: 'IDR'.")
    @SafeString(groups = SafeStringValidation.class, message = "totalAmount.currency must be a string. Ensure that totalAmount.currency is enclosed in quotes. Example: 'IDR'.")
    @Pattern(regexp = "^[a-zA-Z]{3}$", groups = PatternValidation.class, message = "totalAmount.currency must be exactly 3 characters long. Ensure that totalAmount.currency is exactly 3 characters. Example: 'IDR'.")
    private String currency;
}
