package com.doku.sdk.dokujavalibrary.dto.request;

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
public class RequestHeaderDto {
    private String xTimestamp;
    private String xSignature;
    private String xPartnerId;
    private String xExternalId;
    private String xDeviceId;
    private String xIpAddress;
    private String channelId;
    private String authorization;
}
