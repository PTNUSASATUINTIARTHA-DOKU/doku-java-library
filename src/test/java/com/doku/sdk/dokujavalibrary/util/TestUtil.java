package com.doku.sdk.dokujavalibrary.util;

import com.doku.sdk.dokujavalibrary.dto.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response.AccountBindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.request.AccountUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.response.AccountUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.request.BalanceInquiryRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.response.BalanceInquiryResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request.BankCardData;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request.CardRegistrationRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.response.CardRegistrationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.request.CardUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.response.CardUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.request.CheckStatusRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.response.CheckStatusResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request.PaymentJumpAppRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response.PaymentJumpAppResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request.PaymentRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.response.PaymentResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request.RefundRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.response.RefundResponseDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.CheckStatusVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.BillAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusResponseAdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestAdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseAdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.UpdateVaAdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;

import java.util.List;

public class TestUtil {

    public static final String CLIENT_ID = "clientId";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    public static final String TOKEN_B2B = "tokenB2b";
    public static final String SECRET_KEY = "secretKey";
    public static final String DEVICE_ID = "deviceId";
    public static final String IP_ADDRESS = "100.100.100.100";


    public TokenB2BResponseDto getTokenB2BResponseDto(String responseCode) {
        return TokenB2BResponseDto.builder()
                .responseCode(responseCode)
                .responseMessage("Successful")
                .accessToken(TOKEN_B2B)
                .tokenType("Bearer ")
                .expiresIn("890")
                .build();
    }

    public CreateVaRequestDto getCreateVaRequestDto() {
        return CreateVaRequestDto.builder()
                .partnerServiceId("    1899")
                .customerNo("20240704001")
                .virtualAccountNo("    189920240704001")
                .virtualAccountName("SDK TEST")
                .virtualAccountEmail("sdk@email.com")
                .virtualAccountPhone("6281288932399")
                .trxId("INV_20240711001")
                .totalAmount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .additionalInfo(AdditionalInfoDto.builder()
                        .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                        .virtualAccountConfig(VirtualAccountConfigDto.builder()
                                .reusableStatus(false)
                                .build())
                        .build())
                .virtualAccountTrxType("C")
                .expiredDate("2024-07-29T09:54:04+07:00")
                .build();
    }

    public CreateVaResponseDto getCreateVaResponseDto() {
        return CreateVaResponseDto.builder()
                .responseCode("2002700")
                .responseMessage("Successful")
                .virtualAccountData(VirtualAccountDataDto.builder()
                        .partnerServiceId("    1899")
                        .customerNo("20240704001")
                        .virtualAccountNo("    189920240704001")
                        .virtualAccountName("SDK TEST")
                        .virtualAccountEmail("sdk@email.com")
                        .trxId("INV_20240711001")
                        .totalAmount(TotalAmountDto.builder()
                                .value("10000.00")
                                .currency("IDR")
                                .build())
                        .additionalInfo(AdditionalInfoDto.builder()
                                .howToPayPage("howToPayPage")
                                .howToPayApi("howToPayApi")
                                .build())
                        .build())
                .build();
    }

    public UpdateVaRequestDto getUpdateVaRequestDto() {
        return UpdateVaRequestDto.builder()
                .partnerServiceId("    1899")
                .customerNo("000000000650")
                .virtualAccountNo("    1899000000000650")
                .virtualAccountName("SDK TEST")
                .virtualAccountEmail("sdk@email.com")
                .virtualAccountPhone("6281288932399")
                .trxId("INV_20240710001")
                .totalAmount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .additionalInfo(UpdateVaAdditionalInfoDto.builder()
                        .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                        .virtualAccountConfig(UpdateVaAdditionalInfoDto.UpdateVaVirtualAccountConfigDto.builder()
                                .status("ACTIVE")
                                .build())
                        .build())
                .virtualAccountTrxType("C")
                .expiredDate("2024-07-29T09:54:04+07:00")
                .build();
    }

    public UpdateVaResponseDto getUpdateVaResponseDto() {
        return UpdateVaResponseDto.builder()
                .responseCode("2002800")
                .responseMessage("Successful")
                .virtualAccountData(VirtualAccountDataDto.builder()
                        .partnerServiceId("    1899")
                        .customerNo("000000000650")
                        .virtualAccountNo("    1899000000000650")
                        .virtualAccountName("SDK TEST")
                        .virtualAccountEmail("sdk@email.com")
                        .trxId("INV_20240710001")
                        .totalAmount(TotalAmountDto.builder()
                                .value("10000.00")
                                .currency("IDR")
                                .build())
                        .expiredDate("2024-07-29T09:54:04+07:00")
                        .additionalInfo(AdditionalInfoDto.builder()
                                .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                                .virtualAccountConfig(VirtualAccountConfigDto.builder()
                                        .status("INACTIVE")
                                        .build())
                                .build())
                        .build())
                .build();
    }

    public DeleteVaRequestDto getDeleteVaRequestDto() {
        return DeleteVaRequestDto.builder()
                .partnerServiceId("    1899")
                .customerNo("000000000661")
                .virtualAccountNo("    1899000000000661")
                .trxId("INV_20240715001")
                .additionalInfo(DeleteVaRequestAdditionalInfoDto.builder()
                        .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                        .build())
                .build();
    }

    public DeleteVaResponseDto getDeleteVaResponseDto() {
        return DeleteVaResponseDto.builder()
                .responseCode("2003100")
                .responseMessage("Successful")
                .virtualAccountData(DeleteVaResponseVirtualAccountDataDto.builder()
                        .partnerServiceId("    1899")
                        .customerNo("000000000661")
                        .virtualAccountNo("    1899000000000661")
                        .trxId("INV_20240715001")
                        .additionalInfo(DeleteVaResponseAdditionalInfoDto.builder()
                                .channel("VIRTUAL_ACCOUNT_BANK_CIMB")
                                .build())
                        .build())
                .build();
    }

    public CheckStatusVaRequestDto getCheckStatusVaRequestDto() {
        return CheckStatusVaRequestDto.builder()
                .partnerServiceId("    1899")
                .customerNo("000000000661")
                .virtualAccountNo("    1899000000000661")
                .build();
    }

    public CheckStatusVaResponseDto getCheckStatusVaResponseDto() {
        return CheckStatusVaResponseDto.builder()
                .responseCode("2002600")
                .responseMessage("Successful")
                .virtualAccountData(CheckStatusVirtualAccountDataDto.builder()
                        .partnerServiceId("    1899")
                        .customerNo("000000000661")
                        .virtualAccountNo("    1899000000000661")
                        .trxId("INV_20240715001")
                        .paidAmount(TotalAmountDto.builder()
                                .value("10000.00")
                                .currency("IDR")
                                .build())
                        .billDetails(List.of(BillAmountDto.builder()
                                .billAmount(TotalAmountDto.builder().value("1000.00").currency("IDR").build())
                                .build()))
                        .build())
                .additionalInfo(CheckStatusResponseAdditionalInfoDto.builder()
                        .acquirer("BANK_CIMB")
                        .build())
                .build();
    }

    public AccountBindingRequestDto getAccountBindingRequestDto() {
        return AccountBindingRequestDto.builder()
                .phoneNo("6281288932399")
                .additionalInfo(AccountBindingRequestDto.AccountBindingAdditionalInfoRequestDto.builder()
                        .channel("EMONEY_OVO_SNAP")
                        .custIdMerchant("CUST-001")
                        .successRegistrationUrl("https://merchant.doku.com/success")
                        .failedRegistrationUrl("https://merchant.doku.com/failed")
                        .build())
                .build();
    }

    public AccountBindingResponseDto getAccountBindingResponseDto() {
        return AccountBindingResponseDto.builder()
                .responseCode("2000700")
                .responseMessage("Successful")
                .referenceNo("129260743966")
                .redirectUrl("redirectUrl")
                .additionalInfo(AccountBindingResponseDto.AccountBindingAdditionalInfoResponseDto.builder()
                        .custIdMerchant("CUST-001")
                        .accountStatus("PENDING")
                        .authCode("authCode")
                        .build())
                .build();
    }

    public AccountUnbindingRequestDto getAccountUnbindingRequestDto() {
        return AccountUnbindingRequestDto.builder()
                .tokenId("tokenId")
                .additionalInfo(AccountUnbindingRequestDto.AccountUnbindingAdditionalInfoRequestDto.builder()
                        .channel("EMONEY_OVO_SNAP")
                        .build())
                .build();
    }

    public AccountUnbindingResponseDto getAccountUnbindingResponseDto() {
        return AccountUnbindingResponseDto.builder()
                .responseCode("2000900")
                .responseMessage("Successful")
                .referenceNo("UNB-0001")
                .build();
    }

    public CardRegistrationRequestDto getCardRegistrationRequestDto() {
        return CardRegistrationRequestDto.builder()
                .cardData(BankCardData.builder().bankCardNo("12345").bankCardType("D").expiryDate("2025").build())
                .custIdMerchant("cust001")
                .phoneNo("628238748728423")
                .additionalInfo(CardRegistrationRequestDto.CardRegistrationAdditionalInfoRequestDto.builder()
                        .channel("DIRECT_DEBIT_BRI_SNAP")
                        .customerName("John Doe")
                        .email("john.doe@doku.com")
                        .idCard("12345")
                        .country("Indonesia")
                        .address("Bali")
                        .dateOfBirth("19990101")
                        .successRegistrationUrl("https://merchant.doku.com/success")
                        .failedRegistrationUrl("https://merchant.doku.com/failed")
                        .build())
                .build();
    }

    public CardRegistrationResponseDto getCardRegistrationResponseDto() {
        return CardRegistrationResponseDto.builder()
                .responseCode("2000100")
                .responseMessage("Successful")
                .referenceNo("129260743966")
                .redirectUrl("redirectUrl")
                .additionalInfo(CardRegistrationResponseDto.CardRegistrationAdditionalInfoResponseDto.builder()
                        .custIdMerchant("12345679504")
                        .status("PENDING")
                        .authCode("authCode")
                        .build())
                .build();
    }

    public CardUnbindingRequestDto getCardUnbindingRequestDto() {
        return CardUnbindingRequestDto.builder()
                .tokenId("eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE2OTg4MjI3NTQsImlzcyI6IkRPS1UiLCJjbGllbnRJZCI6IkJSTi0wMjAyLTE2OTAyNzUzNTM3OTgiLCJhY2NvdW50SWQiOiJjZTBhZWIyM2YyMmZhOTgxZWViNTE1MjFmZmNkYmUzNyJ9.QZ2z0p2PoCYbuBSId7LleLqTUwNyNIeM1PUSaV4DwGKO05l7xQ3EbpdAPK62hxKNcczKqQqGY2Om6rzS78s2Tj88dkDD2vl46o3xEPd_plqQW8ayFqS74Z_HcFJfdo-egqFv9rAX7qgiE5AJHSx_hFolET9B3o3Jx82lmQutnXOjYb5gW9PV0FCPIZRWOaXppOSJSVcmTvXZxF0KUID9-2QVmQ5aPZroHjShYJKGyUu-1tCPClD_CbZMCi3TxhKLnI3e2oIoK7VjXEsrJjuil8O1zZTT7_aXAGgTu5UcPCrc0U9_3Nj-wQlEjDpedMVypKAWATWBUVpMo2MAsBRDAw")
                .additionalInfo(CardUnbindingRequestDto.CardUnbindingAdditionalInfoRequestDto.builder()
                        .channel("DIRECT_DEBIT_BRI_SNAP")
                        .build())
                .build();
    }

    public CardUnbindingResponseDto getCardUnbindingResponseDto() {
        return CardUnbindingResponseDto.builder()
                .responseCode("2000500")
                .responseMessage("Successful")
                .referenceNo("UNB-0001")
                .redirectUrl("redirectUrl")
                .build();
    }

    public PaymentRequestDto getPaymentRequestDto() {
        return PaymentRequestDto.builder()
                .partnerReferenceNo("INV-0001")
                .feeType("OUR")
                .chargeToken("")
                .amount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .payOptionDetails(List.of(PaymentRequestDto.PayOptionDetailsDto.builder()
                                .payMethod("CASH")
                                .transAmount(TotalAmountDto.builder()
                                        .value("10000.00")
                                        .currency("IDR")
                                        .build())
                                .feeAmount(TotalAmountDto.builder()
                                        .value("10000.00")
                                        .currency("IDR")
                                        .build())
                        .build()))
                .additionalInfo(PaymentRequestDto.PaymentAdditionalInfoRequestDto.builder()
                        .channel("EMONEY_OVO_SNAP")
                        .successPaymentUrl("www.merchant.com/success")
                        .failedPaymentUrl("www.merchant.com/failed")
                        .paymentType("SALE")
                        .build())
                .build();
    }

    public PaymentResponseDto getPaymentResponseDto() {
        return PaymentResponseDto.builder()
                .responseCode("2005400")
                .responseMessage("Successful")
                .webRedirectUrl("webRedirectUrl")
                .partnerReferenceNo("INV-0001")
                .build();
    }

    public PaymentJumpAppRequestDto getPaymentJumpAppRequestDto() {
        return PaymentJumpAppRequestDto.builder()
                .partnerReferenceNo("INV-0001")
                .validUpTo("2024-07-10T11:57:58+07:00")
                .pointOfInitiation("app")
                .urlParam(PaymentJumpAppRequestDto.UrlParamDto.builder()
                        .url("www.merchant.co.id")
                        .type("PAY_RETURN")
                        .isDeepLink("Y")
                        .build())
                .amount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .additionalInfo(PaymentJumpAppRequestDto.PaymentJumpAppAdditionalInfoRequestDto.builder()
                        .channel("EMONEY_DANA_SNAP")
                        .orderTitle("orderTitle")
                        .build())
                .build();
    }

    public PaymentJumpAppResponseDto getPaymentJumpAppResponseDto() {
        return PaymentJumpAppResponseDto.builder()
                .responseCode("2005400")
                .responseMessage("Successful")
                .webRedirectUrl("webRedirectUrl")
                .partnerReferenceNo("INV-0001")
                .build();
    }

    public BalanceInquiryRequestDto getBalanceInquiryRequestDto() {
        return BalanceInquiryRequestDto.builder()
                .additionalInfo(BalanceInquiryRequestDto.BalanceInquiryAdditionalInfoRequestDto.builder()
                        .channel("EMONEY_OVO_SNAP")
                        .build())
                .build();
    }

    public BalanceInquiryResponseDto getBalanceInquiryResponseDto() {
        return BalanceInquiryResponseDto.builder()
                .responseCode("2001100")
                .responseMessage("Successful")
                .accountInfos(List.of(BalanceInquiryResponseDto.AccountInfosDto.builder()
                                .balanceType("CASH")
                                .amount(TotalAmountDto.builder()
                                        .value("10000.00")
                                        .currency("IDR")
                                        .build())
                                .flatAmount(TotalAmountDto.builder()
                                        .value("10000.00")
                                        .currency("IDR")
                                        .build())
                                .holdAmount(TotalAmountDto.builder()
                                        .value("10000.00")
                                        .currency("IDR")
                                        .build())
                        .build()))
                .build();
    }

    public RefundRequestDto getRefundRequestDto() {
        return RefundRequestDto.builder()
                .additionalInfo(RefundRequestDto.RefundAdditionalInfoRequestDto.builder()
                        .channel("EMONEY_OVO_SNAP")
                        .build())
                .originalPartnerReferenceNo("INV-0001")
                .originalExternalId("REQ-0001")
                .refundAmount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .reason("Request by Customer")
                .partnerRefundNo("INV-REF-0001")
                .build();
    }

    public RefundResponseDto getRefundResponseDto() {
        return RefundResponseDto.builder()
                .responseCode("2005800")
                .responseMessage("Successful")
                .refundAmount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .originalPartnerReferenceNo("Ra7o1bLJAh2oV9eb33129stQc5xFm5s7")
                .originalReferenceNo("Ra7o1bLJAh2oV9eb33129stQc5xFm5s7")
                .refundNo("Ra7o1bLJAh2oV9eb33129stQc5xFm5s7")
                .partnerRefundNo("Ra7o1bLJAh2oV9eb33129stQc5xFm5s7")
                .refundTime("2024-01-01T09:09:00.123")
                .build();
    }

    public CheckStatusRequestDto getCheckStatusRequestDto() {
        return CheckStatusRequestDto.builder()
                .originalPartnerReferenceNo("2020102900000000000001")
                .originalReferenceNo("2020102977770000000009")
                .originalExternalId("30443786930722726463280097920912")
                .serviceCode("55")
                .transactionDate("2020-12-21T14:56:11+07:00")
                .amount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .merchantId("23489182303312")
                .subMerchantId("23489182303312")
                .externalStoreId("183908924912387")
                .additionalInfo(CheckStatusRequestDto.CheckStatusAdditionalInfoRequestDto.builder()
                        .deviceId("12345679237")
                        .channel("DIRECT_DEBIT_BRI_SNAP")
                        .build())
                .build();
    }

    public CheckStatusResponseDto getCheckStatusResponseDto() {
        return CheckStatusResponseDto.builder()
                .responseCode("2005500")
                .responseMessage("Successful")
                .originalPartnerReferenceNo("2020102900000000000001")
                .originalReferenceNo("2020102977770000000009")
                .approvalCode("201039000200")
                .originalExternalId("30443786930722726463280097920912")
                .serviceCode("55")
                .latestTransactionStatus("00")
                .transactionStatusDesc("success")
                .originalResponseCode("2005500")
                .originalResponseMessage("Request has been processed successfully")
                .sessionId("883737GHY8839")
                .requestId("3763773")
                .refundHistory(List.of(CheckStatusResponseDto.RefundHistoryDto.builder()
                                .refundNo("96194816941239812")
                                .partnerReferenceNo("239850918204981205970")
                                .refundAmount(TotalAmountDto.builder()
                                        .value("10000.00")
                                        .currency("IDR")
                                        .build())
                                .refundStatus("00")
                                .refundDate("2020-12-23T07:44:16+07:00")
                                .reason("Customer Complain")
                        .build()))
                .transAmount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .feeAmount(TotalAmountDto.builder()
                        .value("10000.00")
                        .currency("IDR")
                        .build())
                .paidTime("2020-12-21T14:56:11+07:00")
                .additionalInfo(CheckStatusResponseDto.CheckStatusAdditionalInfoResponseDto.builder()
                        .deviceId("12345679237")
                        .channel("DIRECT_DEBIT_BRI_SNAP")
                        .build())
                .build();
    }
}
