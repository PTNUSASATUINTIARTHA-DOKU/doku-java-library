package com.doku.sdk.dokujavalibrary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeaderDto {
    private String xTimestamp;
    private String xSignature;
    private String xPartnerId;
    private String xExternalId;
    private String channelId;
    private String authorization;
}
