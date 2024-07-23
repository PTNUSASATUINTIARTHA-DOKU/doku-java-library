package com.doku.sdk.dokujavalibrary.dto.va;

import com.doku.sdk.dokujavalibrary.validation.group.AmountValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VirtualAccountConfigDto {

    private Boolean reusableStatus;
    private String status;

    @Digits(integer = 16, fraction = 2, groups = AmountValidation.class)
    @DecimalMin(value = "1", groups = SizeValidation.class)
    private BigDecimal minAmount;

    @Digits(integer = 16, fraction = 2, groups = AmountValidation.class)
    private BigDecimal maxAmount;
}
