package com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.doku.sdk.dokujavalibrary.validation.annotation.SafeString;
import com.doku.sdk.dokujavalibrary.validation.group.MandatoryValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SafeStringValidation;
import com.doku.sdk.dokujavalibrary.validation.group.SizeValidation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundRequestDto {

    @NotNull(groups = MandatoryValidation.class)
    @NotEmpty(groups = MandatoryValidation.class)
    private String originalPartnerReferenceNo;

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 36, groups = SizeValidation.class)
    private String originalExternalId;

    @Valid
    private TotalAmountDto refundAmount;

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 255, groups = SizeValidation.class)
    private String reason;

    @NotNull(groups = MandatoryValidation.class)
    @SafeString(groups = SafeStringValidation.class)
    private String partnerRefundNo;

    @Valid
    private RefundAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;
        private CreateVaRequestDto.OriginDto origin;
    }

    public void validateRefundRequest(RefundRequestDto refundRequestDto) {
        if (!isValidChannel(refundRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("5000700", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

        if(refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            if(refundRequestDto.originalPartnerReferenceNo.length() < 32 || refundRequestDto.originalPartnerReferenceNo.length() > 64) {
                throw new GeneralException("4005801", "originalPartnerReferenceNo must be 64 characters and at least 32 characters. Ensure that originalPartnerReferenceNo is no longer than 64 characters and at least 32 characters. Example: 'INV-REF-001'.");
            }
        }
        if(refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_CIMB_SNAP.name())||refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_BRI_SNAP.name())) {
            if(refundRequestDto.originalPartnerReferenceNo.length() > 12) {
                throw new GeneralException("4005801", "originalPartnerReferenceNo max 12 characters. Ensure that originalPartnerReferenceNo is no longer than 12 characters. Example: 'INV-001'.");
            }
        }

        if(refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_SHOPEE_PAY_SNAP.name()) || refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_DANA_SNAP.name())) {
            if(refundRequestDto.originalPartnerReferenceNo.length() > 64) {
                throw new GeneralException("4005801", "originalPartnerReferenceNo must be 64 characters or fewer. Ensure that originalPartnerReferenceNo is no longer than 64 characters. Example: 'INV-001'.");
            }
        }

        
        if(refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_SHOPEE_PAY_SNAP.name()) || refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_DANA_SNAP.name()) || refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_OVO_SNAP.name())) {
            if(refundRequestDto.partnerRefundNo.length() > 64) {
                throw new GeneralException("4005801", "partnerRefundNo must be 64 characters or fewer. Ensure that partnerRefundNo is no longer than 64 characters. Example: 'INV-REF-001'.");
            }
        }
        if(refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_CIMB_SNAP.name())||refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_BRI_SNAP.name())) {
            if(refundRequestDto.partnerRefundNo.length() > 12) {
                throw new GeneralException("4005801", "partnerRefundNo must be 12 characters or fewer. Ensure that partnerRefundNo is no longer than 12 characters. Example: 'INV-REF-001'.");
            }
        }
        if(refundRequestDto.getAdditionalInfo().getChannel().equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            if(refundRequestDto.partnerRefundNo.length() > 64 || refundRequestDto.partnerRefundNo.length() < 32) {
                throw new GeneralException("4005801", "partnerRefundNo must be 64 characters and at least 32 characters. Ensure that partnerRefundNo is no longer than 64 characters and at least 32 characters. Example: 'INV-REF-001'.");
            }
        }
        if(originalPartnerReferenceNo == null){
            throw new GeneralException("5000700", "originalPartnerReferenceNo cannot be null. Please provide an originalPartnerReferenceNo. Example: 'INV-0001");
        }

    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }
}
