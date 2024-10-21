package com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCardData {
    private String bankCardNo;
    private String bankCardType;
    private String expiryDate;
    private String identificationNo;
    private String identificationType;
    private String email;
}
