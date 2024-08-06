package com.doku.sdk.dokujavalibrary.common;

import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class SnapUtils {

    public String generateExternalId() {
        UUID uuid = UUID.randomUUID();
        long timestamp = Instant.now().getEpochSecond();

        return uuid.toString() + timestamp;
    }

    public RequestHeaderDto generateRequestHeaderDto(String timestamp,
                                                     String signature,
                                                     String clientId,
                                                     String externalId,
                                                     String deviceId,
                                                     String ipAddress,
                                                     String channelId,
                                                     String tokenB2b) {
        return RequestHeaderDto.builder()
                .xTimestamp(timestamp)
                .xSignature(signature)
                .xPartnerId(clientId)
                .xExternalId(externalId)
                .xDeviceId(deviceId)
                .xIpAddress(ipAddress)
                .channelId(channelId)
                .authorization(tokenB2b)
                .build();
    }
}
