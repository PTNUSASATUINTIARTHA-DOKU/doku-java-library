package com.doku.sdk.dokujavalibrary.module;

import com.doku.sdk.dokujavalibrary.common.ValidationUtils;
import com.doku.sdk.dokujavalibrary.controller.NotificationController;
import com.doku.sdk.dokujavalibrary.controller.TokenController;
import com.doku.sdk.dokujavalibrary.controller.VaController;
import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
@RequiredArgsConstructor
public class DokuSnap {

    private final TokenController tokenController;
    private final VaController vaController;
    private final NotificationController notificationController;

    // global variable?
    private PrivateKey privateKey;
    private String clientId;
    private Boolean isProduction;
    private String tokenB2b;
    private long tokenExpiresIn;
    private long tokenGeneratedTimestamp;
    private String publicKey;
    private String issuer;
    private String secretKey;

    public TokenB2BResponseDto getB2bToken(PrivateKey privateKey, String clientId, Boolean isProduction) {
        return tokenController.getTokenB2B(privateKey, clientId, isProduction);
    }

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, PrivateKey privateKey, String clientId, Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(createVaRequestDto);
        } catch (BadRequestException e) {
            return CreateVaResponseDto.builder()
                    .responseCode(e.getResponseCode())
                    .responseMessage(e.getMessage())
                    .build();
        }
        createVaRequestDto.validateCreateVaRequestDto(createVaRequestDto);

        Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
        if (tokenInvalid) {
            tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
        }

        return vaController.createVa(createVaRequestDto, privateKey, clientId, tokenB2b, isProduction);
    }

    // function name is recommended to be different
    public CreateVaResponseDto createVaV1(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        CreateVaRequestDto createVaRequestDto = vaController.convertToCreateVaRequestDto(createVaRequestDtoV1);

        return createVa(createVaRequestDto, privateKey, clientId, isProduction);
    }

    public Boolean validateTokenB2b(String requestTokenB2b, String publicKey) {
        return tokenController.validateTokenB2b(requestTokenB2b, publicKey);
    }

    public Boolean validateSignature(String requestSignature, String requestTimestamp, PrivateKey privateKey, String clientId) {
        return tokenController.validateSignature(requestSignature, requestTimestamp, privateKey, clientId);
    }

    public NotificationTokenDto validateSignatureAndGenerateToken(String requestSignature, String requestTimestamp, PrivateKey privateKey, String clientId) {
        Boolean isSignatureValid = validateSignature(requestSignature, requestTimestamp, privateKey, clientId);
        return generateTokenB2b(isSignatureValid);
    }

    public NotificationTokenDto generateTokenB2b(Boolean isSignatureValid) {
        String parsedTokenExpiresIn = String.valueOf(tokenExpiresIn);
        String parsedTokenGeneratedTimestamp = String.valueOf(tokenGeneratedTimestamp);

        if (isSignatureValid) {
            return tokenController.generateTokenB2b(parsedTokenExpiresIn, issuer, privateKey, clientId, parsedTokenGeneratedTimestamp);
        } else {
            return tokenController.generateInvalidSignatureResponse();
        }
    }

    public PaymentNotificationResponseDto generateNotificationResponse(Boolean isTokenValid, PaymentNotificationRequestBodyDto paymentNotificationRequestBodyDto) {
        if (isTokenValid) {
            if (paymentNotificationRequestBodyDto != null) {
                return notificationController.generateNotificationResponse(paymentNotificationRequestBodyDto);
            } else {
                throw new BadRequestException(HttpStatus.BAD_REQUEST.name(), "If token is valid, please provide PaymentNotificationRequestBodyDto");
            }
        }
        return notificationController.generateInvalidTokenResponse();
    }

    public PaymentNotificationResponseDto validateTokenAndGenerateNotificationResponse(String requestTokenB2b, PaymentNotificationRequestBodyDto paymentNotificationRequestBodyDto, String publicKey) {
        Boolean isValid = validateTokenB2b(requestTokenB2b, publicKey);
        return generateNotificationResponse(isValid, paymentNotificationRequestBodyDto);
    }

    public RequestHeaderDto generateRequestHeader() {
        Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
        if (tokenInvalid) {
            tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
        }

        return tokenController.doGenerateRequestHeader(privateKey, clientId, tokenB2b);
    }

    public UpdateVaResponseDto updateVa(UpdateVaDto updateVaDto, PrivateKey privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(updateVaDto);
        } catch (BadRequestException e) {
            return UpdateVaResponseDto.builder()
                    .responseCode(e.getResponseCode())
                    .responseMessage(e.getMessage())
                    .build();
        }
        updateVaDto.validateUpdateVaRequestDto(updateVaDto);

        Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
        if (tokenInvalid) {
            tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
        }

        return vaController.doUpdateVa(updateVaDto, clientId, tokenB2b, secretKey, isProduction);
    }

    public DeleteVaResponseDto deletePaymentCode(DeleteVaRequestDto deleteVaRequestDto, PrivateKey privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(deleteVaRequestDto);
        } catch (BadRequestException e) {
            return DeleteVaResponseDto.builder()
                    .responseCode(e.getResponseCode())
                    .responseMessage(e.getMessage())
                    .build();
        }
        deleteVaRequestDto.validateDeleteVaRequestDto(deleteVaRequestDto);

        Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
        if (tokenInvalid) {
            tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
        }

        return vaController.doDeletePaymentCode(deleteVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
    }
}
