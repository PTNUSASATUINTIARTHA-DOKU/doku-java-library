package com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response;

import com.doku.sdk.dokujavalibrary.dto.directdebit.LineItemsDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request.PaymentRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentJumpAppResponseDto {
    private String responseCode;
    private String responseMessage;
    private String webRedirectUrl;
    private String partnerReferenceNo;
    @Valid
    private PaymentAdditionalInfoResponseDto additionalInfo;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAdditionalInfoResponseDto {
        private String webRedirectUrl;
    }
}
