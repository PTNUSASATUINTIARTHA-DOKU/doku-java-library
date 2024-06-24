package com.doku.sdk.dokujavalibrary.dto.va.deleteva.response;

import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
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
public class DeleteVaResponseAdditionalInfoDto {
    private String channel;
    private VirtualAccountConfigDto virtualAccountConfig;
}
