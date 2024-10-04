package com.doku.sdk.dokujavalibrary.dto.va.deleteva.request;

import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.enums.VaChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.doku.sdk.dokujavalibrary.exception.SimulatorException;
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

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class, message = "customerNo must be a string. Ensure that customerNo is enclosed in quotes. Example: '00000000000000000001'.")
    @Length(max = 20, groups = SizeValidation.class, message = "customerNo must be 20 characters or fewer. Ensure that customerNo is no longer than 20 characters. Example: '00000000000000000001'.")
    @Pattern(regexp = "^[0-9]*$", groups = PatternValidation.class, message = "customerNo must consist of only digits. Ensure that customerNo contains only numbers. Example: '00000000000000000001'.")
    private String customerNo;

    @NotNull(groups = MandatoryValidation.class, message = "virtualAccountNo cannot be null. Please provide a virtualAccountNo. Example: ' 88899400000000000000000001'.")
    @SafeString(groups = SafeStringValidation.class, message = "virtualAccountNo must be a string. Ensure that virtualAccountNo is enclosed in quotes. Example: ' 88899400000000000000000001'.")
    private String virtualAccountNo;

    @NotNull(groups = MandatoryValidation.class, message = "trxId cannot be null. Please provide a trxId. Example: '23219829713'.")
    @SafeString(groups = SafeStringValidation.class, message = "trxId must be a string. Ensure that trxId is enclosed in quotes. Example: '23219829713'.")
    @Size(min = 1, groups = SizeValidation.class, message = "trxId must be at least 1 character long. Ensure that trxId is not empty. Example: '23219829713'.")
    @Size(max = 64, groups = SizeValidation.class, message = "trxId must be 64 characters or fewer. Ensure that trxId is no longer than 64 characters. Example: '23219829713'.")
    private String trxId;

    @Valid
    private DeleteVaRequestAdditionalInfoDto additionalInfo;

    public void validateDeleteVaRequestDto(DeleteVaRequestDto deleteVaRequestDto) {
        if (!deleteVaRequestDto.getPartnerServiceId().isEmpty()
                && !deleteVaRequestDto.getCustomerNo().isEmpty()
                && !deleteVaRequestDto.getVirtualAccountNo().isEmpty()) {
            String target = deleteVaRequestDto.getPartnerServiceId() + deleteVaRequestDto.getCustomerNo();
            if (!deleteVaRequestDto.getVirtualAccountNo().equals(target)) {
                throw new GeneralException("4003101", "virtualAccountNo must be the concatenation of partnerServiceId and customerNo. Example: ' 88899400000000000000000001' (where partnerServiceId is ' 888994' and customerNo is '00000000000000000001').");
            }
        }

        if (!isValidChannel(deleteVaRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("4003101", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'VIRTUAL_ACCOUNT_MANDIRI'.");
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(VaChannelEnum.values()).anyMatch(vaChannelEnum -> vaChannelEnum.getV2Channel().equals(channel));
    }

    public void validateDeleteVaSimulator(DeleteVaRequestDto deleteVaRequestDto, Boolean isProduction) {
        if (!isProduction) {
            if (deleteVaRequestDto.getTrxId().startsWith("1118") || deleteVaRequestDto.getVirtualAccountNo().trim().startsWith("1118")) {
                var object = DeleteVaResponseVirtualAccountDataDto.builder()
                        .partnerServiceId("90341537")
                        .customerNo("00000077")
                        .virtualAccountNo("9034153700000077")
                        .trxId("PGPWF167")
                        .build();

                throw new SimulatorException("2003100", "Success", object);
            }
            if (deleteVaRequestDto.getTrxId().startsWith("111") || deleteVaRequestDto.getVirtualAccountNo().trim().startsWith("111")) {
                throw new SimulatorException("4013101", "Access Token Invalid (B2B)", null);
            } else if (deleteVaRequestDto.getTrxId().startsWith("112") || deleteVaRequestDto.getVirtualAccountNo().trim().startsWith("112")) {
                throw new SimulatorException("4013100", "Unauthorized . Signature Not Match", null);
            } else if (deleteVaRequestDto.getTrxId().startsWith("113") || deleteVaRequestDto.getVirtualAccountNo().trim().startsWith("113")) {
                var object = DeleteVaResponseVirtualAccountDataDto.builder()
                        .partnerServiceId("")
                        .customerNo("00000077")
                        .virtualAccountNo("9034153700000077")
                        .trxId("PGPWF167")
                        .build();

                throw new SimulatorException("4003102", "Invalid Mandatory Field {partnerServiceId}", object);
            } else if (deleteVaRequestDto.getTrxId().startsWith("114") || deleteVaRequestDto.getVirtualAccountNo().trim().startsWith("114")) {
                var object = DeleteVaResponseVirtualAccountDataDto.builder()
                        .partnerServiceId("90341537")
                        .customerNo("00000077")
                        .virtualAccountNo("virtualAccountNo")
                        .trxId("PGPWF167")
                        .build();

                throw new SimulatorException("4003101", "Invalid Field Format {virtualAccountNo}", object);
            } else if (deleteVaRequestDto.getTrxId().startsWith("115") || deleteVaRequestDto.getVirtualAccountNo().trim().startsWith("115")) {
                throw new SimulatorException("4093100", "Conflict", null);
            }
        }
    }
}
