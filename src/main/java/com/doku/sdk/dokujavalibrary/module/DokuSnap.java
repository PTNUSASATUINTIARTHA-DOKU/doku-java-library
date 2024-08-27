package com.doku.sdk.dokujavalibrary.module;

import com.doku.sdk.dokujavalibrary.common.ValidationUtils;
import com.doku.sdk.dokujavalibrary.controller.DirectDebitController;
import com.doku.sdk.dokujavalibrary.controller.NotificationController;
import com.doku.sdk.dokujavalibrary.controller.TokenController;
import com.doku.sdk.dokujavalibrary.controller.VaController;
import com.doku.sdk.dokujavalibrary.dto.RequestHeaderDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.request.AccountBindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountbinding.response.AccountBindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.request.AccountUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.accountunbinding.response.AccountUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.request.BalanceInquiryRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.balanceinquiry.response.BalanceInquiryResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.request.CardRegistrationRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardregistration.response.CardRegistrationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.request.CheckStatusRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.response.CheckStatusResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request.PaymentJumpAppRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response.PaymentJumpAppResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request.PaymentRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.response.PaymentResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request.RefundRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.response.RefundResponseDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2B2CResponseDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.request.InquiryRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.doku.sdk.dokujavalibrary.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DokuSnap {

    private final TokenController tokenController;
    private final VaController vaController;
    private final DirectDebitController directDebitController;
    private final NotificationController notificationController;

    // global variable?
    private String privateKey;
    private String clientId;
    private Boolean isProduction;
    private String tokenB2b;
    private final long tokenB2bExpiresIn = 900;
    private long tokenB2bGeneratedTimestamp;
    private String tokenB2b2c;
    private final long tokenB2b2cExpiresIn = 900;
    private long tokenB2b2cGeneratedTimestamp;
    private String publicKey;
    private String issuer;
    private String secretKey;

    public TokenB2BResponseDto getB2bToken(String privateKey, String clientId, Boolean isProduction) {
        tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();

        try {
            return tokenController.getTokenB2B(privateKey, clientId, isProduction);
        } catch (Exception e) {
            return TokenB2BResponseDto.builder()
                    .responseCode("5007300")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public TokenB2B2CResponseDto getB2b2cToken(String authCode, String privateKey, String clientId, boolean isProduction) {
        tokenB2b2cGeneratedTimestamp = Instant.now().getEpochSecond();

        try {
            return tokenController.getTokenB2B2C(authCode, privateKey, clientId, isProduction);
        } catch (Exception e) {
            return TokenB2B2CResponseDto.builder()
                    .responseCode("5007300")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, String privateKey, String clientId, Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(createVaRequestDto);
            createVaRequestDto.validateCreateVaRequestDto(createVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
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
            return tokenController.generateTokenB2b(tokenB2bExpiresIn, issuer, privateKey, clientId, timestamp);
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
        Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
        if (tokenInvalid) {
            tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
            tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
        }

        return tokenController.doGenerateRequestHeader(privateKey, clientId, tokenB2b);
    }

    public UpdateVaResponseDto updateVa(UpdateVaRequestDto updateVaRequestDto, String privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(updateVaRequestDto);
            updateVaRequestDto.validateUpdateVaRequestDto(updateVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
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

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
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

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
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

    public InquiryResponseBodyDto directInquiryResponseMapping(String inquiryResponseV1) {
        return vaController.v1ToSnapConverter(inquiryResponseV1);
    }

    public String directInquiryRequestMapping(HttpServletRequest headerRequest, InquiryRequestBodyDto inquiryRequestBodyDto) {
        return vaController.snapToV1Converter(headerRequest, inquiryRequestBodyDto);
    }

    public AccountBindingResponseDto doAccountBinding(AccountBindingRequestDto accountBindingRequestDto,
                                                      String privateKey,
                                                      String clientId,
                                                      boolean isProduction,
                                                      String deviceId,
                                                      String ipAddress) {
        try {
            ValidationUtils.validateRequest(accountBindingRequestDto);
            accountBindingRequestDto.validateAccountBindingRequest(accountBindingRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doAccountBinding(accountBindingRequestDto, secretKey, clientId, deviceId, ipAddress, tokenB2b, isProduction);
        } catch (BadRequestException e) {
            return AccountBindingResponseDto.builder()
                    .responseCode("5000700")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public AccountUnbindingResponseDto doAccountUnbinding(AccountUnbindingRequestDto accountUnbindingRequestDto,
                                                        String privateKey,
                                                        String clientId,
                                                        boolean isProduction,
                                                        String ipAddress) {
        try {
            ValidationUtils.validateRequest(accountUnbindingRequestDto);
            accountUnbindingRequestDto.validateAccountUnbindingRequest(accountUnbindingRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doAccountUnbinding(accountUnbindingRequestDto, secretKey, clientId, ipAddress, tokenB2b, isProduction);
        } catch (BadRequestException e) {
            return AccountUnbindingResponseDto.builder()
                    .responseCode("5000900")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public CardRegistrationResponseDto doCardRegistration(CardRegistrationRequestDto cardRegistrationRequestDto,
                                                          String privateKey,
                                                          String clientId,
                                                          String channelId,
                                                          boolean isProduction) {
        try {
            ValidationUtils.validateRequest(cardRegistrationRequestDto);
            cardRegistrationRequestDto.validateCardRegistrationRequest(cardRegistrationRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doCardRegistration(cardRegistrationRequestDto, secretKey, clientId, channelId, tokenB2b, isProduction);
        } catch (BadRequestException e) {
            return CardRegistrationResponseDto.builder()
                    .responseCode("5000100")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public PaymentResponseDto doPayment(PaymentRequestDto paymentRequestDto,
                                        String privateKey,
                                        String clientId,
                                        String ipAddress,
                                        String channelId,
                                        String authCode,
                                        boolean isProduction) {
        try {
            ValidationUtils.validateRequest(paymentRequestDto);
            paymentRequestDto.validatePaymentRequest(paymentRequestDto);

            Boolean tokenB2bInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenB2bInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            Boolean tokenB2b2cInvalid = tokenController.isTokenInvalid(tokenB2b2c, tokenB2b2cExpiresIn, tokenB2b2cGeneratedTimestamp);
            if (tokenB2b2cInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b2c = tokenController.getTokenB2B2C(authCode, privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doPayment(paymentRequestDto, secretKey, clientId, ipAddress, channelId, tokenB2b2c, tokenB2b, isProduction);
        } catch (BadRequestException e) {
            return PaymentResponseDto.builder()
                    .responseCode("5005400")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public PaymentJumpAppResponseDto doPaymentJumpApp(PaymentJumpAppRequestDto paymentJumpAppRequestDto,
                                                      String privateKey,
                                                      String clientId,
                                                      String deviceId,
                                                      String ipAddress,
                                                      boolean isProduction) {
        try {
            ValidationUtils.validateRequest(paymentJumpAppRequestDto);
            paymentJumpAppRequestDto.validatePaymentJumpAppRequest(paymentJumpAppRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doPaymentJumpApp(paymentJumpAppRequestDto, secretKey, clientId, deviceId, ipAddress, tokenB2b, isProduction);
        } catch (BadRequestException e) {
            return PaymentJumpAppResponseDto.builder()
                    .responseCode("5005400")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public RefundResponseDto doRefund(RefundRequestDto refundRequestDto,
                                      String privateKey,
                                      String clientId,
                                      String ipAddress,
                                      String authCode,
                                      boolean isProduction) {
        try {
            ValidationUtils.validateRequest(refundRequestDto);
            refundRequestDto.validateRefundRequest(refundRequestDto);

            Boolean tokenB2bInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenB2bInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            Boolean tokenB2b2cInvalid = tokenController.isTokenInvalid(tokenB2b2c, tokenB2b2cExpiresIn, tokenB2b2cGeneratedTimestamp);
            if (tokenB2b2cInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b2c = tokenController.getTokenB2B2C(authCode, privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doRefund(refundRequestDto, secretKey, clientId, ipAddress, tokenB2b, tokenB2b2c, isProduction);
        } catch (BadRequestException e) {
            return RefundResponseDto.builder()
                    .responseCode("5005800")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public BalanceInquiryResponseDto doBalanceInquiry(BalanceInquiryRequestDto balanceInquiryRequestDto,
                                                      String privateKey,
                                                      String clientId,
                                                      String ipAddress,
                                                      String authCode,
                                                      boolean isProduction) {
        try {
            ValidationUtils.validateRequest(balanceInquiryRequestDto);
            balanceInquiryRequestDto.validateBalanceInquiryRequest(balanceInquiryRequestDto);

            Boolean tokenB2bInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenB2bInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            Boolean tokenB2b2cInvalid = tokenController.isTokenInvalid(tokenB2b2c, tokenB2b2cExpiresIn, tokenB2b2cGeneratedTimestamp);
            if (tokenB2b2cInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b2c = tokenController.getTokenB2B2C(authCode, privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doBalanceInquiry(balanceInquiryRequestDto, secretKey, clientId, ipAddress, tokenB2b, tokenB2b2c, isProduction);
        } catch (BadRequestException e) {
            return BalanceInquiryResponseDto.builder()
                    .responseCode("5001100")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public CheckStatusResponseDto doCheckStatus(CheckStatusRequestDto checkStatusRequestDto,
                                                String privateKey,
                                                String clientId,
                                                boolean isProduction) {
        try {
            ValidationUtils.validateRequest(checkStatusRequestDto);
            checkStatusRequestDto.validateCheckStatusRequest(checkStatusRequestDto);

            Boolean tokenB2bInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenB2bInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doCheckStatus(checkStatusRequestDto, secretKey, clientId, tokenB2b, isProduction);
        } catch (BadRequestException e) {
            return CheckStatusResponseDto.builder()
                    .responseCode("5005500")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

}
