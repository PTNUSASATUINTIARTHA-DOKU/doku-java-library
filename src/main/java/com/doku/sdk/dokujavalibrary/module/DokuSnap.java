package com.doku.sdk.dokujavalibrary.module;

import com.doku.sdk.dokujavalibrary.common.ValidationUtils;
import com.doku.sdk.dokujavalibrary.controller.NotificationController;
import com.doku.sdk.dokujavalibrary.controller.TokenController;
import com.doku.sdk.dokujavalibrary.controller.VaController;
import com.doku.sdk.dokujavalibrary.dto.request.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DokuSnap {

    private final TokenController tokenController;
    private final VaController vaController;
    private final NotificationController notificationController;

    // global variable?
    private String privateKey;
    private String clientId;
    private Boolean isProduction;
    private String tokenB2b;
    private final long tokenExpiresIn = 900;
    private long tokenGeneratedTimestamp;
    private String publicKey;
    private String issuer;
    private String secretKey;

    public TokenB2BResponseDto getB2bToken(String privateKey, String clientId, Boolean isProduction) {
        tokenGeneratedTimestamp = Instant.now().getEpochSecond();

        try {
            return tokenController.getTokenB2B(privateKey, clientId, isProduction);
        } catch (Exception e) {
            return TokenB2BResponseDto.builder()
                    .responseCode("5007300")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, String privateKey, String clientId, Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(createVaRequestDto);
            createVaRequestDto.validateCreateVaRequestDto(createVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
            if (tokenInvalid) {
                tokenGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.createVa(createVaRequestDto, privateKey, clientId, tokenB2b, isProduction);
        } catch (BadRequestException e) {
            return CreateVaResponseDto.builder()
                    .responseCode("5002700")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    // function name is recommended to be different
    public CreateVaResponseDto createVaV1(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        CreateVaRequestDto createVaRequestDto = vaController.convertToCreateVaRequestDto(createVaRequestDtoV1);

        return createVa(createVaRequestDto, privateKey, clientId, isProduction);
    }

    public Boolean validateTokenB2b(String requestTokenB2b, String publicKey) {
        return tokenController.validateTokenB2b(requestTokenB2b, publicKey);
    }

    private Boolean validateAsymmetricSignature(String requestSignature, String requestTimestamp, String privateKey, String clientId) {
        return tokenController.validateAsymmetricSignature(requestSignature, requestTimestamp, privateKey, clientId);
    }

    public NotificationTokenDto validateAsymmetricSignatureAndGenerateToken(String requestSignature, String requestTimestamp, String privateKey, String clientId) {
        Boolean isSignatureValid = validateAsymmetricSignature(requestSignature, requestTimestamp, privateKey, clientId);
        return generateTokenB2b(isSignatureValid, privateKey, clientId, requestTimestamp);
    }

    public NotificationTokenDto generateTokenB2b(Boolean isSignatureValid, String privateKey, String clientId, String timestamp) {
        if (isSignatureValid) {
            return tokenController.generateTokenB2b(tokenExpiresIn, issuer, privateKey, clientId, timestamp);
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
            tokenGeneratedTimestamp = Instant.now().getEpochSecond();
            tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
        }

        return tokenController.doGenerateRequestHeader(privateKey, clientId, tokenB2b);
    }

    public UpdateVaResponseDto updateVa(UpdateVaRequestDto updateVaRequestDto, String privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(updateVaRequestDto);
            updateVaRequestDto.validateUpdateVaRequestDto(updateVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
            if (tokenInvalid) {
                tokenGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.doUpdateVa(updateVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
        } catch (BadRequestException e) {
            return UpdateVaResponseDto.builder()
                    .responseCode("5002800")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public DeleteVaResponseDto deletePaymentCode(DeleteVaRequestDto deleteVaRequestDto, String privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(deleteVaRequestDto);
            deleteVaRequestDto.validateDeleteVaRequestDto(deleteVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
            if (tokenInvalid) {
                tokenGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.doDeletePaymentCode(deleteVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
        } catch (BadRequestException e) {
            return DeleteVaResponseDto.builder()
                    .responseCode("5003100")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public CheckStatusVaResponseDto checkStatusVa(CheckStatusVaRequestDto checkStatusVaRequestDto, String privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(checkStatusVaRequestDto);
            checkStatusVaRequestDto.validateCheckStatusVaRequestDto(checkStatusVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenExpiresIn, tokenGeneratedTimestamp);
            if (tokenInvalid) {
                tokenGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.doCheckStatusVa(checkStatusVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
        } catch (BadRequestException e) {
            return CheckStatusVaResponseDto.builder()
                    .responseCode("5002600")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

}
