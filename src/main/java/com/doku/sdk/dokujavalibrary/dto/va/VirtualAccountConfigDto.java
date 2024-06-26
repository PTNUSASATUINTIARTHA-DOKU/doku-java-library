package com.doku.sdk.dokujavalibrary.dto.va;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VirtualAccountConfigDto {

    private Boolean reusableStatus;
    private String status;
    private String minAmount;
    private String maxAmount;
}
