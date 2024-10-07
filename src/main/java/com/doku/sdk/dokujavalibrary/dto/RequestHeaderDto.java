package com.doku.sdk.dokujavalibrary.dto;

import com.doku.sdk.dokujavalibrary.enums.DirectDebitChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestHeaderDto {
    private String xTimestamp;
    private String xSignature;
    private String xPartnerId;
    private String xExternalId;
    private String xDeviceId;
    private String xIpAddress;
    private String channelId;
    private String authorizationCustomer;
    private String authorization;

    public  void validateDeviceId(String serviceCode) {
        if(xDeviceId == null) {
            throw new GeneralException("400"+serviceCode+"01", "X-DEVICE-ID must be 64 characters or fewer. Ensure that X-DEVICE-ID is no longer than 64 characters.");
        }

        if(xDeviceId.length() > 64) {
            throw new GeneralException("400"+serviceCode+"01", "X-DEVICE-ID must be 64 characters or fewer. Ensure that X-DEVICE-ID is no longer than 64 characters.");
        }
    }

    public void validateIpAddress(String serviceCode) {
        if(xIpAddress == null) {
            throw new GeneralException("400"+serviceCode+"01", "X-IP-ADDRESS must be in 10 to 15 characters.");
        }

        if(xIpAddress.length() < 10 || xIpAddress.length() > 15) {
            throw new GeneralException("400"+serviceCode+"01", "X-IP-ADDRESS must be in 10 to 15 characters.");
        }
    }

    public void validateAccountBindingHeader(String channel) {
        if (channel.equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            validateDeviceId("07");
            validateIpAddress("07");
        }
    }

    public void validatePaymentHeader(String channel) {
        if (channel.equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            validateIpAddress("54");
        } else if (channel.equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_DANA_SNAP.name())) {
            validateDeviceId("54");
            validateIpAddress("54");
        } else if (channel.equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_SHOPEE_PAY_SNAP.name())) {
            validateDeviceId("54");
            validateIpAddress("54");
        }
    }

    public void validateBalanceInquiryHeader(String channel) {
        if (channel.equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            validateIpAddress("11");
        }
    }

    public void validateAccountUnbindingHeader(String channel) {
        if (channel.equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            validateIpAddress("05");
        }
    }

    public void validateRefundHeader(String channel) {
        if (channel.equalsIgnoreCase(DirectDebitChannelEnum.DIRECT_DEBIT_ALLO_SNAP.name())) {
            validateIpAddress("07");
        } else if (channel.equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_DANA_SNAP.name())) {
            validateDeviceId("07");
            validateIpAddress("07");
        } else if (channel.equalsIgnoreCase(DirectDebitChannelEnum.EMONEY_SHOPEE_PAY_SNAP.name())) {
            validateDeviceId("07");
            validateIpAddress("07");
        }
    }

}
