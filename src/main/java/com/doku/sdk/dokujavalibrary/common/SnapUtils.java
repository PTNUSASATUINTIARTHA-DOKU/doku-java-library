package com.doku.sdk.dokujavalibrary.common;

import com.doku.sdk.dokujavalibrary.dto.RequestHeaderDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SnapUtils {

    public String generateExternalId() {
        long timestamp = Instant.now().getEpochSecond();

        return RandomStringUtils.randomNumeric(16) + timestamp;
    }

    public RequestHeaderDto generateRequestHeaderDto(String timestamp,
                                                     String signature,
                                                     String clientId,
                                                     String externalId,
                                                     String deviceId,
                                                     String ipAddress,
                                                     String channelId,
                                                     String tokenB2b2c,
                                                     String tokenB2b) {
        return RequestHeaderDto.builder()
                .xTimestamp(timestamp)
                .xSignature(signature)
                .xPartnerId(clientId)
                .xExternalId(externalId)
                .xDeviceId(deviceId)
                .xIpAddress(ipAddress)
                .channelId(channelId)
                .authorizationCustomer(tokenB2b2c)
                .authorization("Bearer " + tokenB2b)
                .build();
    }
}
