package com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response;

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
public class CheckStatusResponseAdditionalInfoDto {
    private Object acquirer;
}
