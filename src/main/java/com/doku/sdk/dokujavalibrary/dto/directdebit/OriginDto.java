package com.doku.sdk.dokujavalibrary.dto.directdebit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OriginDto {
    private String source;
    private String system;
    private String product;
    private String apiFormat;
}
