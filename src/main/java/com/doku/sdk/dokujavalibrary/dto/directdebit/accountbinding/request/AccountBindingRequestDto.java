package com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request;

import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
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

    private String phoneNo;

    @Valid
    private AccountBindingAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBindingAdditionalInfoRequestDto {
        @SafeString(groups = SafeStringValidation.class)
        private String channel;

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
//        @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$\n", groups = PatternValidation.class)
        private String dateOfBirth;

        private String successRegistrationUrl;

        private String failedRegistrationUrl;

        @SafeString(groups = SafeStringValidation.class)
        private String deviceModel; // allo

        @SafeString(groups = SafeStringValidation.class)
        private String osType; // allo

        @SafeString(groups = SafeStringValidation.class)
        private String channelId; // allo
    }

    public void validateAccountBindingRequest(AccountBindingRequestDto accountBindingRequestDto) {
        if (accountBindingRequestDto.getPhoneNo() == null || accountBindingRequestDto.getPhoneNo().isEmpty()) {
            throw new GeneralException("4000701", "phoneNo cannot be null. Please provide a phoneNo. Example: '62813941306101'.");
        }
        if (accountBindingRequestDto.getPhoneNo().length() < 9) {
            throw new GeneralException("4000701", "phoneNo must be at least 9 digits. Ensure that phoneNo is not empty. Example: '62813941306101'.");
        }
        if (accountBindingRequestDto.getPhoneNo().length() > 16) {
            throw new GeneralException("4000701", "phoneNo must be 16 characters or fewer. Ensure that phoneNo is no longer than 16 characters. Example: '62813941306101'.");
        }
        if (accountBindingRequestDto.getAdditionalInfo().getChannel() == null || accountBindingRequestDto.getAdditionalInfo().getChannel().isEmpty() ) {
            throw new GeneralException("4000704", "additionalInfo.channel cannot be null. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }else
        if (!isValidChannel(accountBindingRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("4000701", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }
        if (accountBindingRequestDto.getAdditionalInfo().getCustIdMerchant() == null || accountBindingRequestDto.getAdditionalInfo().getCustIdMerchant().isEmpty()) {
            throw new GeneralException("4000701", "additionalInfo.custIdMerchant cannot be null or empty. Please provide a additionalInfo.custIdMerchant. Example: 'cust-001'.");
        }
        if (accountBindingRequestDto.getAdditionalInfo().getCustIdMerchant().length() > 64 ) {
            throw new GeneralException("4000701", "additionalInfo.custIdMerchant must be 64 characters or fewer. Ensure that additionalInfo.custIdMerchant is no longer than 64 characters. Example: 'cust-001'.");
        }

        if (accountBindingRequestDto.getAdditionalInfo().getSuccessRegistrationUrl() == null) {
            throw new GeneralException("4000702", "additionalInfo.successRegistrationUrl cannot be null. Please provide a additionalInfo.successRegistrationUrl. Example: 'https://www.doku.com'.");
        }
        
        if (accountBindingRequestDto.getAdditionalInfo().getFailedRegistrationUrl() == null) {
            throw new GeneralException("4000703", "additionalInfo.failedRegistrationUrl cannot be null. Please provide a additionalInfo.failedRegistrationUrl. Example: 'https://www.doku.com'.");
        }
        
       
        if (accountBindingRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            if (accountBindingRequestDto.getAdditionalInfo().getDeviceModel().isEmpty() ||
            accountBindingRequestDto.getAdditionalInfo().getOsType().isEmpty() ||
            accountBindingRequestDto.getAdditionalInfo().getChannelId().isEmpty()) {
                throw new GeneralException("4000701", "Value cannot be null for DIRECT_DEBIT_ALLO_SNAP");
            }

            if (!accountBindingRequestDto.getAdditionalInfo().getOsType().equalsIgnoreCase("ios") &&
            !accountBindingRequestDto.getAdditionalInfo().getOsType().equalsIgnoreCase("android")) {
                throw new GeneralException("4000701", "osType value can only be ios/android");
            }

            if (!accountBindingRequestDto.getAdditionalInfo().getChannelId().equalsIgnoreCase("app") &&
                    !accountBindingRequestDto.getAdditionalInfo().getChannelId().equalsIgnoreCase("web")) {
                throw new GeneralException("4000701", "channelId value can only be app/web");
            }
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
