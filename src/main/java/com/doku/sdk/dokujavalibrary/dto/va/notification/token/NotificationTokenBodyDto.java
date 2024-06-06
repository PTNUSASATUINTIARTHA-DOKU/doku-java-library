package com.doku.sdk.dokujavalibrary.dto.va.notification.token;

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
public class NotificationTokenBodyDto {

    private String responseCode;
    private String responseMessage;
    private String accessToken;
    private String tokenType;
    private String expiresIn;
    private String additionalInfo;
}
