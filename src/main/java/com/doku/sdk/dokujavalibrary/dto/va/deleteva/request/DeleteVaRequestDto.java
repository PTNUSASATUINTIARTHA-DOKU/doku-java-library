package com.doku.sdk.dokujavalibrary.dto.va.deleteva.request;

import com.doku.sdk.dokujavalibrary.enums.VaChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import com.doku.sdk.dokujavalibrary.validation.annotation.FixedLength;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.annotation.VirtualAccountNo;
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteVaRequestDto {

    @NotNull(groups = MandatoryValidation.class, message = "partnerServiceId cannot be null. Please provide a partnerServiceId. Example: ' 888994'.")
    @SafeString(groups = SafeStringValidation.class, message = "partnerServiceId must be a string. Ensure that partnerServiceId is enclosed in quotes. Example: ' 888994'.")
    @FixedLength(length = {8}, groups = LengthValidation.class, message = "partnerServiceId must be exactly 8 characters long. Ensure that partnerServiceId has 8 characters, left-padded with spaces. Example: ' 888994'.")
    @Pattern(regexp = "^\\s{0,7}\\d{1,8}$?", groups = PatternValidation.class, message = "partnerServiceId must consist of up to 7 spaces followed by 1 to 8 digits. Make sure partnerServiceId follows this format. Example: ' 888994' (2 spaces and 6 digits).")
    private String partnerServiceId;

    @SafeString(groups = SafeStringValidation.class)
    @Length(max = 20, groups = SizeValidation.class)
    @Pattern(regexp = "^\\d+$")
    private String customerNo;

    @SafeString(groups = SafeStringValidation.class)
    @VirtualAccountNo
    private String virtualAccountNo;

    @NotNull(groups = MandatoryValidation.class, message = "trxId cannot be null. Please provide a trxId. Example: '23219829713'.")
    @SafeString(groups = SafeStringValidation.class, message = "trxId must be a string. Ensure that trxId is enclosed in quotes. Example: '23219829713'.")
    @Size(min = 1, groups = SizeValidation.class, message = "trxId must be at least 1 character long. Ensure that trxId is not empty. Example: '23219829713'.")
    @Size(max = 64, groups = SizeValidation.class, message = "trxId must be 64 characters or fewer. Ensure that trxId is no longer than 64 characters. Example: '23219829713'.")
    private String trxId;

    private DeleteVaRequestAdditionalInfoDto additionalInfo;

    public void validateDeleteVaRequestDto(DeleteVaRequestDto deleteVaRequestDto) {
        if (isValidChannel(deleteVaRequestDto.getAdditionalInfo().getChannel()) == false) {
            throw new BadRequestException("", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.");
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(VaChannelEnum.values()).anyMatch(vaChannelEnum -> vaChannelEnum.name().equals(channel));
    }
}
