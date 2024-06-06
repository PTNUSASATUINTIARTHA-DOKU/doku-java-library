package com.doku.sdk.dokujavalibrary.dto.va.notification.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTokenDto {
    private NotificationTokenHeaderDto header;
    private NotificationTokenBodyDto body;
}
