package com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.LineItemsDto;
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
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequestDto {

    @SafeString(groups = SafeStringValidation.class)
    @Size(max = 64, groups = SizeValidation.class)
    private String partnerReferenceNo;

    @NotNull(groups = MandatoryValidation.class)
    @Size(max = 32, groups = SizeValidation.class)
    private  String chargeToken;

    @SafeString(groups = SafeStringValidation.class)
    private String feeType; // ovo

    @Valid
    private TotalAmountDto amount;

    @Valid
    private List<PayOptionDetailsDto> payOptionDetails; // allo, ovo

    @Valid
    private PaymentAdditionalInfoRequestDto additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayOptionDetailsDto {
        @SafeString(groups = SafeStringValidation.class)
        private String payMethod;

        @Valid
        private TotalAmountDto transAmount;

        @Valid
        private TotalAmountDto feeAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAdditionalInfoRequestDto {
        @NotNull(groups = MandatoryValidation.class)
        @SafeString(groups = SafeStringValidation.class)
        private String channel;

        @SafeString(groups = SafeStringValidation.class)
        @Size(max = 40, groups = SizeValidation.class)
        private String remarks; // allo, cimb
        @NotNull(groups = MandatoryValidation.class)
        private String successPaymentUrl;
        @NotNull(groups = MandatoryValidation.class)
        private String failedPaymentUrl;

        @Valid
        private List<LineItemsDto> lineItems; // allo

        @SafeString(groups = SafeStringValidation.class)
        private String paymentType; // bri, ovo

        private CreateVaRequestDto.OriginDto origin;
    }

    public void validatePaymentRequest(PaymentRequestDto paymentRequestDto) {
        if(ObjectUtils.isEmpty(paymentRequestDto.getPartnerReferenceNo())){
            throw new GeneralException("4005402", "Invalid Mandatory Field partnerReferenceNo");
        }

        if(paymentRequestDto.getPartnerReferenceNo().length() < 3 || paymentRequestDto.getPartnerReferenceNo().length() > 12){
            throw new GeneralException("4045418", "Inconsistent Request");
        }
        if (!isValidChannel(paymentRequestDto.getAdditionalInfo().getChannel())) {
            throw new GeneralException("4005401", "additionalInfo.channel is not valid. Ensure that additionalInfo.channel is one of the valid channels. Example: 'DIRECT_DEBIT_ALLO_SNAP'.");
        }

        if(!isValidURL(paymentRequestDto.getAdditionalInfo().getSuccessPaymentUrl())){
            throw new GeneralException("4045418", "Inconsistent Request");
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.EMONEY_OVO_SNAP.name())) {
            if (ObjectUtils.isNotEmpty(paymentRequestDto.getFeeType())) {
                if (!paymentRequestDto.getFeeType().equalsIgnoreCase("OUR") &&
                        !paymentRequestDto.getFeeType().equalsIgnoreCase("BEN") &&
                        !paymentRequestDto.getFeeType().equalsIgnoreCase("SHA")) {
                    throw new GeneralException("4005401", "Value can only be OUR/BEN/SHA for EMONEY_OVO_SNAP");
                }
            }

            if (paymentRequestDto.getPayOptionDetails().isEmpty()) {
                throw new GeneralException("4005401", "Pay Option Details cannot be empty for EMONEY_OVO_SNAP");
            }

            if (ObjectUtils.isNotEmpty(paymentRequestDto.getAdditionalInfo().getPaymentType())) {
                if (!paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("SALE") &&
                        !paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("RECURRING")) {
                    throw new GeneralException("4005401", "additionalInfo.paymentType cannot be empty for EMONEY_OVO_SNAP");
                }
            }
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            if (paymentRequestDto.getAdditionalInfo().getLineItems().isEmpty()) {
                throw new GeneralException("4005401", "additionalInfo.lineItems cannot be empty for DIRECT_DEBIT_ALLO_SNAP");
            }

            if (paymentRequestDto.getAdditionalInfo().getRemarks().isEmpty()) {
                throw new GeneralException("4005401", "additionalInfo.remarks cannot be empty for DIRECT_DEBIT_ALLO_SNAP");
            }
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_CIMB_SNAP.name())) {
            if (paymentRequestDto.getAdditionalInfo().getRemarks().isEmpty()) {
                throw new GeneralException("4005401", "additionalInfo.remarks cannot be empty for DIRECT_DEBIT_CIMB_SNAP");
            }
        }

        if (paymentRequestDto.getAdditionalInfo().getChannel().equals(DirectDebitChannelEnum.DIRECT_DEBIT_BRI_SNAP.name())) {
            if (ObjectUtils.isNotEmpty(paymentRequestDto.getAdditionalInfo().getPaymentType())) {
                if (!paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("SALE") &&
                        !paymentRequestDto.getAdditionalInfo().getPaymentType().equalsIgnoreCase("RECURRING")) {
                    throw new GeneralException("4005401", "additionalInfo.paymentType cannot be empty for DIRECT_DEBIT_BRI_SNAP");
                }
            }
        }
    }

    private static boolean isValidChannel(String channel) {
        return Arrays.stream(DirectDebitChannelEnum.values()).anyMatch(ddChannelEnum -> ddChannelEnum.name().equals(channel));
    }

    private static boolean isValidURL(String url){
            String regex = "^[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            //^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]
            return url.matches(regex);
    }
}
