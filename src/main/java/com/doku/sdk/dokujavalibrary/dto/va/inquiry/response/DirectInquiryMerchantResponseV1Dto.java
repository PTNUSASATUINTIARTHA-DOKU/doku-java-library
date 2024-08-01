package com.doku.sdk.dokujavalibrary.dto.va.inquiry.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "INQUIRY_RESPONSE")
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectInquiryMerchantResponseV1Dto {
    @JsonProperty("PAYMENTCODE")
    private String paymentCode;
    @JsonProperty("AMOUNT")
    private String amount;
    @JsonProperty("PURCHASEAMOUNT")
    private String purchaseAmount;
    @JsonProperty("MINAMOUNT")
    private String minAmount;
    @JsonProperty("MAXAMOUNT")
    private String maxAmount;
    @JsonProperty("TRANSIDMERCHANT")
    private String transIdMerchant;
    @JsonProperty("WORDS")
    private String words;
    @JsonProperty("REQUESTDATETIME")
    private String requestDatetime;
    @JsonProperty("CURRENCY")
    private String currency;
    @JsonProperty("PURCHASECURRENCY")
    private String purchaseCurrency;
    @JsonProperty("SESSIONID")
    private String sessionId;
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("EMAIL")
    private String email;
    @JsonProperty("BASKET")
    private String basket;
    @JsonProperty("ADDITIONALDATA")
    private String additionalData;
    @JsonProperty("RESPONSECODE")
    private String responseCode;
}
