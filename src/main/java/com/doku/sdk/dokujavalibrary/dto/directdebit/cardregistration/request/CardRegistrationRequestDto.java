package com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request;

import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.PatternValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRegistrationRequestDto {
    private String cardData;

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 64, groups = SizeValidation.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String custIdMerchant;

    @NotNull(groups = MandatoryValidation.class)
    @Size(min = 9, groups = SizeValidation.class)
    @Size(max = 16, groups = SizeValidation.class)
    private String phoneNo;

    @Valid
    private CardRegistrationAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardRegistrationAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 70, groups = SizeValidation.class)
        private String customerName;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 64, groups = SizeValidation.class)
        @Email(groups = PatternValidation.class, regexp = ".+[@].+[\\.].+")
        private String email;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 20, groups = SizeValidation.class)
        private String idCard;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 60, groups = SizeValidation.class)
        private String country;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 255, groups = SizeValidation.class)
        private String address;

        @SafeString(groups = SafeStringValidation.class)
        @Pattern(regexp = "^(19|20)\\d\\d(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$\n", groups = PatternValidation.class)
        private String dateOfBirth;

        private String successRegistrationUrl;

        private String failedRegistrationUrl;
    }

    public void validateCardRegistrationRequest(CardRegistrationRequestDto cardRegistrationRequestDto) {
        if (!isValidChannel(cardRegistrationRequestDto.getAdditionalInfo().getChannel())) {
            throw new BadRequestException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
