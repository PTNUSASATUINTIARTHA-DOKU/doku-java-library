package com.doku.sdk.dokujavalibrary.dto.va.createva.request;

import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.enums.VaChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import com.doku.sdk.dokujavalibrary.validation.annotation.DateIso8601;
import com.doku.sdk.dokujavalibrary.validation.annotation.FixedLength;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.LengthValidation;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.PatternValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
public class CreateVaRequestDto {

    @NotNull(groups = MandatoryValidation.class, message = "partnerServiceId cannot be null. Please provide a partnerServiceId. Example: ' 888994'.")
    @SafeString(groups = SafeStringValidation.class, message = "partnerServiceId must be a string. Ensure that partnerServiceId is enclosed in quotes. Example: ' 888994'.")
    @FixedLength(length = {8}, groups = LengthValidation.class, message = "partnerServiceId must be exactly 8 characters long. Ensure that partnerServiceId has 8 characters, left-padded with spaces. Example: ' 888994'.")
    @Pattern(regexp = "^\\s{0,7}\\d{1,8}$?", groups = PatternValidation.class, message = "partnerServiceId must consist of up to 7 spaces followed by 1 to 8 digits. Make sure partnerServiceId follows this format. Example: ' 888994' (2 spaces and 6 digits).")
    private String partnerServiceId;

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class, message = "customerNo must be a string. Ensure that customerNo is enclosed in quotes. Example: '00000000000000000001'.")
    @Length(max = 20, groups = SizeValidation.class, message = "customerNo must be 20 characters or fewer. Ensure that customerNo is no longer than 20 characters. Example: '00000000000000000001'.")
    @Pattern(regexp = "^[0-9]*$", groups = PatternValidation.class, message = "customerNo must consist of only digits. Ensure that customerNo contains only numbers. Example: '00000000000000000001'.")
    private String customerNo;

    @NotNull(groups = MandatoryValidation.class, message = "virtualAccountNo cannot be null. Please provide a virtualAccountNo. Example: ' 88899400000000000000000001'.")
    @SafeString(groups = SafeStringValidation.class, message = "virtualAccountNo must be a string. Ensure that virtualAccountNo is enclosed in quotes. Example: ' 88899400000000000000000001'.")
    private String virtualAccountNo;

    @NotNull(groups = MandatoryValidation.class, message = "virtualAccountName cannot be null. Please provide a virtualAccountName. Example: 'Toru Yamashita'.")
    @SafeString(groups = SafeStringValidation.class, message = "virtualAccountName must be a string. Ensure that virtualAccountName is enclosed in quotes. Example: 'Toru Yamashita'.")
    @Size(min = 1, groups = SizeValidation.class, message = "virtualAccountName must be at least 1 character long. Ensure that virtualAccountName is not empty. Example: 'Toru Yamashita'.")
    @Size(max = 255, groups = SizeValidation.class, message = "virtualAccountName must be 255 characters or fewer. Ensure that virtualAccountName is no longer than 255 characters. Example: 'Toru Yamashita'.")
    @Pattern(regexp = "^[a-zA-Z0-9.\\-/+,=_:'@% ]*$", groups = PatternValidation.class, message = "virtualAccountName can only contain letters, numbers, spaces, and the following characters: .\\-/+,=_:'@%. Ensure that virtualAccountName does not contain invalid characters. Example: 'Toru.Yamashita-123'.")
    private String virtualAccountName;

    @SafeString(groups = SafeStringValidation.class, message = "virtualAccountEmail must be a string. Ensure that virtualAccountEmail is enclosed in quotes. Example: 'toru@example.com'.")
    @Size(min = 1, groups = SizeValidation.class, message = "virtualAccountEmail must be at least 1 character long. Ensure that virtualAccountEmail is not empty. Example: 'toru@example.com'.")
    @Size(max = 255, groups = SizeValidation.class, message = "virtualAccountEmail must be 255 characters or fewer. Ensure that virtualAccountEmail is no longer than 255 characters. Example: 'toru@example.com'.")
    @Email(groups = PatternValidation.class, regexp = ".+[@].+[\\.].+")
    private String virtualAccountEmail;

    @SafeString(groups = SafeStringValidation.class, message = "virtualAccountPhone must be a string. Ensure that virtualAccountPhone is enclosed in quotes. Example: '628123456789'.")
    @Size(min = 9, groups = SizeValidation.class, message = "virtualAccountPhone must be at least 9 characters long. Ensure that virtualAccountPhone is at least 9 characters long. Example: '628123456789'.")
    @Size(max = 30, groups = SizeValidation.class, message = "virtualAccountPhone must be 30 characters or fewer. Ensure that virtualAccountPhone is no longer than 30 characters. Example: '628123456789012345678901234567'.")
    private String virtualAccountPhone;

    @NotNull(groups = MandatoryValidation.class, message = "trxId cannot be null. Please provide a trxId. Example: '23219829713'.")
    @SafeString(groups = SafeStringValidation.class, message = "trxId must be a string. Ensure that trxId is enclosed in quotes. Example: '23219829713'.")
    @Size(min = 1, groups = SizeValidation.class, message = "trxId must be at least 1 character long. Ensure that trxId is not empty. Example: '23219829713'.")
    @Size(max = 64, groups = SizeValidation.class, message = "trxId must be 64 characters or fewer. Ensure that trxId is no longer than 64 characters. Example: '23219829713'.")
    private String trxId;

    @Valid
    private TotalAmountDto totalAmount;

    @Valid
    private AdditionalInfoDto additionalInfo;

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class, message = "virtualAccountTrxType must be a string. Ensure that virtualAccountTrxType is enclosed in quotes. Example: 'C'.")
    @FixedLength(length = {1}, groups = LengthValidation.class, message = "virtualAccountTrxType must be exactly 1 character long. Ensure that virtualAccountTrxType is either 'C', 'O', or 'V'. Example: 'C'.")
    private String virtualAccountTrxType;

    @SafeString(groups = SafeStringValidation.class)
    @DateIso8601(groups = PatternValidation.class, message = "expiredDate must be in ISO-8601 format. Ensure that expiredDate follows the correct format. Example: '2023-01-01T10:55:00+07:00'.")
    private String expiredDate;

    private OriginDto origin;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OriginDto {
        private String product;
        private String source;
        private String sourceVersion;
        private String system;
        private String apiFormat;
    }

    public void validateCreateVaRequestDto(CreateVaRequestDto createVaRequestDto) {
        if (!createVaRequestDto.getPartnerServiceId().isEmpty()
                && !createVaRequestDto.getCustomerNo().isEmpty()
                && !createVaRequestDto.getVirtualAccountNo().isEmpty()) {
            String target = createVaRequestDto.getPartnerServiceId() + createVaRequestDto.getCustomerNo();
            if (!createVaRequestDto.getVirtualAccountNo().equals(target)) {
                throw new BadRequestException("", "virtualAccountNo must be the concatenation of partnerServiceId and customerNo. Example: ' 88899400000000000000000001' (where partnerServiceId is ' 888994' and customerNo is '00000000000000000001').");
            }
        }

        if (!createVaRequestDto.getTotalAmount().getCurrency().equals("IDR")) {
            throw new BadRequestException("", "totalAmount.currency must be 'IDR'. Ensure that totalAmount.currency is 'IDR'. Example: 'IDR'.");
        }

        if (!createVaRequestDto.getVirtualAccountTrxType().equals("C") &&
                !createVaRequestDto.getVirtualAccountTrxType().equals("O") &&
                !createVaRequestDto.getVirtualAccountTrxType().equals("V")) {
            throw new BadRequestException("", "virtualAccountTrxType must be either 'C', 'O', or 'V'. Ensure that virtualAccountTrxType is one of these values. Example: 'C'.");
        }

        if (createVaRequestDto.getAdditionalInfo().getVirtualAccountConfig() != null) {
            if (createVaRequestDto.getAdditionalInfo().getVirtualAccountConfig().getReusableStatus() == null) {
                createVaRequestDto.getAdditionalInfo().getVirtualAccountConfig().setReusableStatus(false);
            }
        }

        if (!isValidChannel(createVaRequestDto.getAdditionalInfo().getChannel())) {
            throw new BadRequestException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.");
        }

        if (createVaRequestDto.getAdditionalInfo().getVirtualAccountConfig().getMinAmount() != null &&
                createVaRequestDto.getAdditionalInfo().getVirtualAccountConfig().getMaxAmount() != null) {
            if (createVaRequestDto.getVirtualAccountTrxType().equals("C")) {
                throw new BadRequestException("", "Only supported for virtualAccountTrxType O and V only");
            }

            if (createVaRequestDto.getAdditionalInfo().getVirtualAccountConfig().getMinAmount().compareTo(createVaRequestDto.getAdditionalInfo().getVirtualAccountConfig().getMaxAmount()) >= 0) {
                throw new BadRequestException("", "maxAmount cannot be lesser than minAmount");
            }
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(VaChannelEnum.values()).anyMatch(vaChannelEnum -> vaChannelEnum.name().equals(channel));
    }
}
