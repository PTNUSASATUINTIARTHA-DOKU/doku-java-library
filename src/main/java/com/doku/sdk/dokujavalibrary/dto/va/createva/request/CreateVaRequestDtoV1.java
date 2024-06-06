package com.doku.sdk.dokujavalibrary.dto.va.createva.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVaRequestDtoV1 {

    private String mallId;
    private String amount;
    private String purchaseAmount;
    private String transIdMerchant;
    private String paymentType;
    private String words;
    private String requestDateTime;
    private String currency;
    private String purchaseCurrency;
    private String sessionId;
    private String name;
    private String email;
    private String additionalData;
    private String basket;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingCountry;
    private String shippingZipcode;
    private String paymentChannel;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String homephone;
    private String mobilephone;
    private String workphone;
    private String birthday;
    private String partnerServiceId;
    private String expiredDate;
}
