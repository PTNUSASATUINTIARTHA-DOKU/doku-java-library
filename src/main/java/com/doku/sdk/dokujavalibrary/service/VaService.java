package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.va.AdditionalInfoDto;
import com.doku.sdk.dokujavalibrary.dto.va.TotalAmountDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountConfigDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.request.InquiryRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.DirectInquiryMerchantResponseV1Dto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.doku.sdk.dokujavalibrary.enums.VaChannelEnum;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VaService {

    private final ConnectionUtils connectionUtils;
    private final Gson gson;

    public CreateVaResponseDto createVa(RequestHeaderDto requestHeaderDto, CreateVaRequestDto createVaRequestDto, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        createVaRequestDto.setOrigin(CreateVaRequestDto.OriginDto.builder()
                        .product("SDK")
                        .source("Java")
                        .sourceVersion("1.0.0")
                        .system("doku-java-library")
                        .apiFormat("SNAP")
                .build());

        String url = SdkConfig.getCreateVaUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(createVaRequestDto));

        return gson.fromJson(response.getBody(), CreateVaResponseDto.class);
    }

    public CreateVaRequestDto convertToCreateVaRequestDto(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        return CreateVaRequestDto.builder()
                .partnerServiceId(createVaRequestDtoV1.getPartnerServiceId())
                .virtualAccountName(createVaRequestDtoV1.getName())
                .virtualAccountEmail(createVaRequestDtoV1.getEmail())
                .virtualAccountPhone(createVaRequestDtoV1.getMobilephone())
                .trxId(createVaRequestDtoV1.getTransIdMerchant())
                .totalAmount(TotalAmountDto.builder()
                        .value(createVaRequestDtoV1.getAmount())
                        .currency(createVaRequestDtoV1.getCurrency())
                        .build())
                .additionalInfo(AdditionalInfoDto.builder()
                        .channel(createVaRequestDtoV1.getPaymentChannel())
                        .build())
                .virtualAccountTrxType("1")
                .expiredDate(createVaRequestDtoV1.getExpiredDate())
                .build();
    }

    public UpdateVaResponseDto doUpdateVa(RequestHeaderDto requestHeaderDto, UpdateVaRequestDto updateVaRequestDto, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getUpdateVaUrl(isProduction);
        var response = connectionUtils.httpPut(url, httpHeaders, gson.toJson(updateVaRequestDto));

        return gson.fromJson(response.getBody(), UpdateVaResponseDto.class);
    }

    public DeleteVaResponseDto doDeletePaymentCode(RequestHeaderDto requestHeaderDto, DeleteVaRequestDto deleteVaRequestDto, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.CHANNEL_ID, requestHeaderDto.getChannelId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getDeleteVaUrl(isProduction);
        var response = connectionUtils.httpDelete(url, httpHeaders, gson.toJson(deleteVaRequestDto));

        return gson.fromJson(response.getBody(), DeleteVaResponseDto.class);
    }

    public CheckStatusVaResponseDto doCheckStatusVa(RequestHeaderDto requestHeaderDto, CheckStatusVaRequestDto checkStatusVaRequestDto, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, requestHeaderDto.getXTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, requestHeaderDto.getXSignature());
        httpHeaders.set(SnapHeaderConstant.X_PARTNER_ID, requestHeaderDto.getXPartnerId());
        httpHeaders.set(SnapHeaderConstant.X_EXTERNAL_ID, requestHeaderDto.getXExternalId());
        httpHeaders.set(SnapHeaderConstant.BEARER, requestHeaderDto.getAuthorization());

        String url = SdkConfig.getCheckStatusVaUrl(isProduction);
        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(checkStatusVaRequestDto));

        return gson.fromJson(response.getBody(), CheckStatusVaResponseDto.class);
    }

    public InquiryResponseBodyDto directInquiryResponseMapping(String inquiryResponseV1) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            var responseV1 =  xmlMapper.readValue(inquiryResponseV1, DirectInquiryMerchantResponseV1Dto.class);

            if (responseV1.getCurrency().equals("360") || responseV1.getPurchaseCurrency().equals("360")) {
                responseV1.setCurrency("IDR");
            }

            if (responseV1.getResponseCode() != null) {
                if (responseV1.getResponseCode().equals("0000")) {
                    responseV1.setResponseCode("2002400");
                }
                if (responseV1.getResponseCode().equals("3000") ||
                        responseV1.getResponseCode().equals("3001") ||
                        responseV1.getResponseCode().equals("3006")) {
                    responseV1.setResponseCode("4042412");
                }
                if (responseV1.getResponseCode().equals("3002")) {
                    responseV1.setResponseCode("4042414");
                }
                if (responseV1.getResponseCode().equals("3004")) {
                    responseV1.setResponseCode("4032400");
                }
                if (responseV1.getResponseCode().equals("9999")) {
                    responseV1.setResponseCode("5002401");
                }
            }

            InquiryResponseBodyDto inquiryResponseBodyDto = InquiryResponseBodyDto.builder()
                    .responseCode(responseV1.getResponseCode())
                    .virtualAccountData(InquiryResponseVirtualAccountDataDto.builder()
                            .customerNo(responseV1.getPaymentCode())
                            .virtualAccountNo(responseV1.getPaymentCode())
                            .virtualAccountName(responseV1.getName())
                            .virtualAccountEmail(responseV1.getEmail())
//                            .virtualAccountTrxType()
                            .totalAmount(TotalAmountDto.builder()
                                    .value(responseV1.getAmount())
                                    .currency(responseV1.getCurrency())
                                    .build())
                            .additionalInfo(InquiryResponseVirtualAccountDataDto.InquiryResponseAdditionalInfoDto.builder()
                                    .trxId(responseV1.getTransIdMerchant())
                                    .virtualAccountConfig(VirtualAccountConfigDto.builder()
                                            .minAmount(new BigDecimal(responseV1.getMinAmount()))
                                            .maxAmount(new BigDecimal(responseV1.getMaxAmount()))
                                            .build())
                                    .build())
                            .build())
                    .build();

            return inquiryResponseBodyDto;
        } catch (JsonProcessingException e) {
            throw new BadRequestException("", e.getMessage());
        }
    }

    public String directInquiryRequestMapping(HttpServletRequest headerRequest, InquiryRequestBodyDto jsonRequest) {
        String paymentChannel = VaChannelEnum.findByV2Channel(jsonRequest.getAdditionalInfo().getChannel()).getOcoChannelId();

        Map<String, String> snapToV1 = new HashMap<>();
        snapToV1.put("MALLID", headerRequest.getHeader(SnapHeaderConstant.X_PARTNER_ID));
        snapToV1.put("PAYMENTCHANNEL", paymentChannel);
        snapToV1.put("PAYMENTCODE", jsonRequest.getVirtualAccountNo());
        snapToV1.put("STATUSTYPE", "/");
        snapToV1.put("OCOID", jsonRequest.getInquiryRequestId());

        StringBuilder v1FormData = new StringBuilder();
        for (Map.Entry<String, String> entry : snapToV1.entrySet()) {
            if (v1FormData.length() > 0) {
                v1FormData.append("&");
            }

            try {
                v1FormData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            } catch (Exception e) {
                throw new BadRequestException("", e.getMessage());
            }
        }
        return v1FormData.toString();
    }
}
