package com.doku.sdk.dokujavalibrary.dto.va;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAccountConfigDto {

    private Boolean reusableStatus;
}
