package com.doku.sdk.dokujavalibrary.util;

import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.CheckStatusVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
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

import java.math.BigDecimal;

public class TestUtil {

    public static final String CLIENT_ID = "clientId";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    public static final String TOKEN_B2B = "tokenB2b";
    public static final String SECRET_KEY = "secretKey";


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
                        .billAmount(TotalAmountDto.builder()
                                .value("10000.00")
                                .currency("IDR")
                                .build())
                        .additionalInfo(CheckStatusResponseAdditionalInfoDto.builder()
                                .acquirer("BANK_CIMB")
                                .build())
                        .build())
                .build();
    }
}
