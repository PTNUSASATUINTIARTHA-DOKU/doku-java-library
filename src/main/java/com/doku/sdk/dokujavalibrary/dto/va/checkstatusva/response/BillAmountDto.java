package com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillAmountDto {
    private TotalAmountDto billAmount;
}
