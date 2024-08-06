package com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request;

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
public class AccountBindingRequestDto {

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    @Size(min = 9, groups = SizeValidation.class)
    @Size(max = 16, groups = SizeValidation.class)
    private String phoneNo;

    @Valid
    private AccountBindingAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBindingAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 64, groups = SizeValidation.class)
        private String custIdMerchant;

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

        @SafeString(groups = SafeStringValidation.class)
        private String deviceModel;

        @SafeString(groups = SafeStringValidation.class)
        private String osType;

        @SafeString(groups = SafeStringValidation.class)
        private String channelId;
    }

    public void validateAccountBindingRequest(AccountBindingRequestDto accountBindingRequestDto) {
        if (!isValidChannel(accountBindingRequestDto.getAdditionalInfo().getChannel())) {
            throw new BadRequestException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

        if (accountBindingRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            if (accountBindingRequestDto.getAdditionalInfo().getDeviceModel().isEmpty() ||
            accountBindingRequestDto.getAdditionalInfo().getOsType().isEmpty() ||
            accountBindingRequestDto.getAdditionalInfo().getChannelId().isEmpty()) {
                throw new BadRequestException("", "Value cannot be null for DIRECT_DEBIT_ALLO_SNAP");
            }

            if (!accountBindingRequestDto.getAdditionalInfo().getOsType().equalsIgnoreCase("ios") &&
            !accountBindingRequestDto.getAdditionalInfo().getOsType().equalsIgnoreCase("android")) {
                throw new BadRequestException("", "osType value can only be ios/android");
            }

            if (!accountBindingRequestDto.getAdditionalInfo().getChannelId().equalsIgnoreCase("app") &&
                    !accountBindingRequestDto.getAdditionalInfo().getChannelId().equalsIgnoreCase("web")) {
                throw new BadRequestException("", "channelId value can only be app/web");
            }
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
