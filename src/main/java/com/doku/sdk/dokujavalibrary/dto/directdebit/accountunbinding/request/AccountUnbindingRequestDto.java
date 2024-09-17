package com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.request;

import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUnbindingRequestDto {
    private String tokenId;
    private AccountUnbindingAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountUnbindingAdditionalInfoRequestDto {
        private String channel;
    }

    public void validateAccountUnbindingRequest(AccountUnbindingRequestDto accountUnbindingRequestDto) {
        if (!isValidChannel(accountUnbindingRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
