package com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.CheckStatusVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusResponsePaymentFlagReasonDto;
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckStatusVaRequestDto {

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

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 128, groups = SizeValidation.class)
    private String inquiryRequestId;

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 128, groups = SizeValidation.class)
    private String paymentRequestId;

    private Object additionalInfo;

    public void validateCheckStatusVaRequestDto(CheckStatusVaRequestDto checkStatusVaRequestDto) {
        if (!checkStatusVaRequestDto.getPartnerServiceId().isEmpty()
                && !checkStatusVaRequestDto.getCustomerNo().isEmpty()
                && !checkStatusVaRequestDto.getVirtualAccountNo().isEmpty()) {
            String target = checkStatusVaRequestDto.getPartnerServiceId() + checkStatusVaRequestDto.getCustomerNo();
            if (!checkStatusVaRequestDto.getVirtualAccountNo().equals(target)) {
                throw new GeneralException("4002601", "virtualAccountNo must be the concatenation of partnerServiceId and customerNo. Example: ' 88899400000000000000000001' (where partnerServiceId is ' 888994' and customerNo is '00000000000000000001').");
            }
        }
    }

    public void validateCheckStatusVaSimulator(CheckStatusVaRequestDto checkStatusVaRequestDto, Boolean isProduction) {
        if (!isProduction) {
            if (checkStatusVaRequestDto.getVirtualAccountNo().trim().startsWith("1113") || checkStatusVaRequestDto.getVirtualAccountNo().trim().startsWith("1116")) {
                var object = CheckStatusVirtualAccountDataDto.builder()
                        .paymentFlagReason(CheckStatusResponsePaymentFlagReasonDto.builder()
                                .english("Pending")
                                .indonesia("Belum Terbayar")
                                .build())
                        .partnerServiceId(checkStatusVaRequestDto.getPartnerServiceId())
                        .customerNo(checkStatusVaRequestDto.getCustomerNo())
                        .virtualAccountNo(checkStatusVaRequestDto.getVirtualAccountNo())
                        .paidAmount(TotalAmountDto.builder()
                                .value("12500.00")
                                .currency("IDR")
                                .build())
                        .build();

                throw new SimulatorException("2002600", "Success", object);
            }
        }
    }
}
