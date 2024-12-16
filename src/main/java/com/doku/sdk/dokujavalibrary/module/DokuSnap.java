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
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.request.CardUnbindingRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.cardunbinding.response.CardUnbindingResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.request.CheckStatusRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.checkstatus.response.CheckStatusResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.request.PaymentJumpAppRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.jumpapp.response.PaymentJumpAppResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.notification.request.DirectDebitNotificationRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.notification.response.DirectDebitNotificationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.request.PaymentRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.payment.response.PaymentResponseDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.request.RefundRequestDto;
import com.doku.sdk.dokujavalibrary.dto.directdebit.refund.response.RefundResponseDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2B2CResponseDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.VirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.CheckStatusVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.request.CheckStatusVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.checkstatusva.response.CheckStatusVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.createva.request.CreateVaRequestDtoV1;
import com.doku.sdk.dokujavalibrary.dto.va.createva.response.CreateVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.request.DeleteVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.deleteva.response.DeleteVaResponseVirtualAccountDataDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.request.InquiryRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.inquiry.response.InquiryResponseBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationRequestBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.payment.PaymentNotificationResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.request.UpdateVaRequestDto;
import com.doku.sdk.dokujavalibrary.dto.va.updateva.response.UpdateVaResponseDto;
import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.doku.sdk.dokujavalibrary.exception.SimulatorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
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
                    .responseCode("5007400")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public CreateVaResponseDto createVa(CreateVaRequestDto createVaRequestDto, String privateKey, String clientId, String secretKey, Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(createVaRequestDto, "27");
            createVaRequestDto.validateCreateVaSimulator(createVaRequestDto, isProduction);
            createVaRequestDto.validateCreateVaRequestDto(createVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.createVa(createVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
        } catch (SimulatorException se) {
            return CreateVaResponseDto.builder()
                    .responseCode(se.getResponseCode())
                    .responseMessage(se.getMessage())
                    .virtualAccountData((VirtualAccountDataDto) se.getObject())
                    .build();
        } catch (GeneralException ge) {
            return CreateVaResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public CreateVaResponseDto createVaV1(CreateVaRequestDtoV1 createVaRequestDtoV1) {
        CreateVaRequestDto createVaRequestDto = vaController.convertToCreateVaRequestDto(createVaRequestDtoV1);

        return createVa(createVaRequestDto, privateKey, clientId, secretKey, isProduction);
    }

    public Boolean validateToken(String requestToken, String publicKey) {
        return tokenController.validateToken(requestToken, publicKey);
    }

    private Boolean validateSignature(String clientId, String requestTimestamp, String requestSignature, String publicKey) {
        return tokenController.validateSignature(clientId, requestTimestamp, requestSignature, publicKey);
    }

    public NotificationTokenDto validateSignatureAndGenerateToken(String clientId, String requestTimestamp, String requestSignature, String publicKey, String privateKey) {
        Boolean isSignatureValid = validateSignature(clientId, requestTimestamp, requestSignature, publicKey);
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
                throw new GeneralException(HttpStatus.BAD_REQUEST.name(), "If token is valid, please provide PaymentNotificationRequestBodyDto");
            }
        }
        return notificationController.generateInvalidTokenResponse();
    }

    public PaymentNotificationResponseDto validateTokenAndGenerateNotificationResponse(String requestTokenB2b, PaymentNotificationRequestBodyDto paymentNotificationRequestBodyDto, String publicKey) {
        Boolean isValid = validateToken(requestTokenB2b, publicKey);
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
            ValidationUtils.validateRequest(updateVaRequestDto, "28");
            updateVaRequestDto.validateUpdateVaSimulator(updateVaRequestDto, isProduction);
            updateVaRequestDto.validateUpdateVaRequestDto(updateVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.doUpdateVa(updateVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
        } catch (SimulatorException se) {
            return UpdateVaResponseDto.builder()
                    .responseCode(se.getResponseCode())
                    .responseMessage(se.getMessage())
                    .virtualAccountData((VirtualAccountDataDto) se.getObject())
                    .build();
        } catch (GeneralException ge) {
            return UpdateVaResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public DeleteVaResponseDto deletePaymentCode(DeleteVaRequestDto deleteVaRequestDto, String privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(deleteVaRequestDto, "31");
            deleteVaRequestDto.validateDeleteVaSimulator(deleteVaRequestDto, isProduction);
            deleteVaRequestDto.validateDeleteVaRequestDto(deleteVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.doDeletePaymentCode(deleteVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
        } catch (SimulatorException se) {
            return DeleteVaResponseDto.builder()
                    .responseCode(se.getResponseCode())
                    .responseMessage(se.getMessage())
                    .virtualAccountData((DeleteVaResponseVirtualAccountDataDto) se.getObject())
                    .build();
        } catch (GeneralException ge) {
            return DeleteVaResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public CheckStatusVaResponseDto checkStatusVa(CheckStatusVaRequestDto checkStatusVaRequestDto, String privateKey, String clientId, String secretKey, boolean isProduction) {
        try {
            ValidationUtils.validateRequest(checkStatusVaRequestDto, "26");
            checkStatusVaRequestDto.validateCheckStatusVaSimulator(checkStatusVaRequestDto, isProduction);
            checkStatusVaRequestDto.validateCheckStatusVaRequestDto(checkStatusVaRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return vaController.doCheckStatusVa(checkStatusVaRequestDto, clientId, tokenB2b, secretKey, isProduction);
        } catch (SimulatorException se) {
            return CheckStatusVaResponseDto.builder()
                    .responseCode(se.getResponseCode())
                    .responseMessage(se.getMessage())
                    .virtualAccountData((CheckStatusVirtualAccountDataDto) se.getObject())
                    .build();
        } catch (GeneralException ge) {
            return CheckStatusVaResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public InquiryResponseBodyDto directInquiryResponseMapping(String inquiryResponseV1) {
        return vaController.v1ToSnapConverter(inquiryResponseV1);
    }

    public String directInquiryRequestMapping(HttpServletRequest headerRequest, InquiryRequestBodyDto inquiryRequestBodyDto) {
        return vaController.snapToV1Converter(headerRequest, inquiryRequestBodyDto);
    }

    public String convertNotificationToV1(PaymentNotificationRequestBodyDto paymentNotificationRequestBodyDto) {
        return vaController.vaPaymentNotificationConverter(paymentNotificationRequestBodyDto);
    }

    public AccountBindingResponseDto doAccountBinding(AccountBindingRequestDto accountBindingRequestDto,
                                                      String privateKey,
                                                      String secretKey,
                                                      String clientId,
                                                      Boolean isProduction,
                                                      String deviceId,
                                                      String ipAddress) {
        try {
            ValidationUtils.validateRequest(accountBindingRequestDto, "07");
            accountBindingRequestDto.validateAccountBindingRequest(accountBindingRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doAccountBinding(accountBindingRequestDto, secretKey, clientId, deviceId, ipAddress, tokenB2b, isProduction);
        } catch (GeneralException ge) {
            return AccountBindingResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public AccountUnbindingResponseDto doAccountUnbinding(AccountUnbindingRequestDto accountUnbindingRequestDto,
                                                        String privateKey,
                                                        String secretKey,
                                                        String clientId,
                                                        Boolean isProduction,
                                                        String ipAddress) {
        try {
            ValidationUtils.validateRequest(accountUnbindingRequestDto, "09");
            accountUnbindingRequestDto.validateAccountUnbindingRequest(accountUnbindingRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doAccountUnbinding(accountUnbindingRequestDto, secretKey, clientId, ipAddress, tokenB2b, isProduction);
        } catch (GeneralException ge) {
            return AccountUnbindingResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public CardRegistrationResponseDto doCardRegistration(CardRegistrationRequestDto cardRegistrationRequestDto,
                                                          String privateKey,
                                                          String secretKey,
                                                          String clientId,
                                                          String channelId,
                                                          Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(cardRegistrationRequestDto, "01");
            cardRegistrationRequestDto.validateCardRegistrationRequest(cardRegistrationRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doCardRegistration(cardRegistrationRequestDto, secretKey, clientId, channelId, tokenB2b, isProduction);
        } catch (GeneralException ge) {
            return CardRegistrationResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public CardUnbindingResponseDto doCardUnbinding(CardUnbindingRequestDto cardUnbindingRequestDto,
                                                    String privateKey,
                                                    String secretKey,
                                                    String clientId,
                                                    Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(cardUnbindingRequestDto, "05");
            cardUnbindingRequestDto.validateCardUnbindingRequest(cardUnbindingRequestDto);

            Boolean tokenB2bInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenB2bInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doCardUnbinding(cardUnbindingRequestDto, secretKey, clientId, tokenB2b, isProduction);
        } catch (GeneralException ge) {
            return CardUnbindingResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public PaymentResponseDto doPayment(PaymentRequestDto paymentRequestDto,
                                        String privateKey,
                                        String secretKey,
                                        String clientId,
                                        String ipAddress,
                                        String channelId,
                                        String authCode,
                                        Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(paymentRequestDto, "54");
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
        } catch (GeneralException ge) {
            return PaymentResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public PaymentJumpAppResponseDto doPaymentJumpApp(PaymentJumpAppRequestDto paymentJumpAppRequestDto,
                                                      String privateKey,
                                                      String secretKey,
                                                      String clientId,
                                                      String deviceId,
                                                      String ipAddress,
                                                      Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(paymentJumpAppRequestDto, "54");
            paymentJumpAppRequestDto.validatePaymentJumpAppRequest(paymentJumpAppRequestDto);

            Boolean tokenInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doPaymentJumpApp(paymentJumpAppRequestDto, secretKey, clientId, deviceId, ipAddress, tokenB2b, isProduction);
        } catch (GeneralException ge) {
            return PaymentJumpAppResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public RefundResponseDto doRefund(RefundRequestDto refundRequestDto,
                                      String privateKey,
                                      String secretKey,
                                      String clientId,
                                      String ipAddress,
                                      String authCode,
                                      Boolean isProduction,
                                      String deviceId) {
        try {
            ValidationUtils.validateRequest(refundRequestDto, "07");
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

            return directDebitController.doRefund(refundRequestDto, secretKey, clientId, ipAddress, tokenB2b, tokenB2b2c, isProduction, deviceId);
        } catch (GeneralException ge) {
            return RefundResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public BalanceInquiryResponseDto doBalanceInquiry(BalanceInquiryRequestDto balanceInquiryRequestDto,
                                                      String privateKey,
                                                      String secretKey,
                                                      String clientId,
                                                      String ipAddress,
                                                      String authCode,
                                                      Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(balanceInquiryRequestDto, "11");
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
        } catch (GeneralException ge) {
            return BalanceInquiryResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public CheckStatusResponseDto doCheckStatus(CheckStatusRequestDto checkStatusRequestDto,
                                                String privateKey,
                                                String secretKey,
                                                String clientId,
                                                Boolean isProduction) {
        try {
            ValidationUtils.validateRequest(checkStatusRequestDto, "55");
            checkStatusRequestDto.validateCheckStatusRequest(checkStatusRequestDto);

            Boolean tokenB2bInvalid = tokenController.isTokenInvalid(tokenB2b, tokenB2bExpiresIn, tokenB2bGeneratedTimestamp);
            if (tokenB2bInvalid) {
                tokenB2bGeneratedTimestamp = Instant.now().getEpochSecond();
                tokenB2b = tokenController.getTokenB2B(privateKey, clientId, isProduction).getAccessToken();
            }

            return directDebitController.doCheckStatus(checkStatusRequestDto, secretKey, clientId, tokenB2b, isProduction);
        } catch (GeneralException ge) {
            return CheckStatusResponseDto.builder()
                    .responseCode(ge.getResponseCode())
                    .responseMessage(ge.getMessage())
                    .build();
        }
    }

    public DirectDebitNotificationResponseDto directDebitPaymentNotification(String requestTokenB2b2c,
                                                                             DirectDebitNotificationRequestDto directDebitNotificationRequestDto,
                                                                             String publicKey) {
        Boolean isTokenB2b2cValid = validateToken(requestTokenB2b2c, publicKey);
        return generateDirectDebitNotificationResponse(isTokenB2b2cValid, directDebitNotificationRequestDto);
    }

    private DirectDebitNotificationResponseDto generateDirectDebitNotificationResponse(Boolean isTokenB2b2cValid,
                                                                                       DirectDebitNotificationRequestDto directDebitNotificationRequestDto) {
        if (isTokenB2b2cValid) {
            if (directDebitNotificationRequestDto != null) {
                return notificationController.generateDirectDebitNotificationResponse(directDebitNotificationRequestDto);
            } else {
                throw new GeneralException(HttpStatus.BAD_REQUEST.name(), "If token is valid, please provide DirectDebitNotificationRequestDto");
            }
        }
        return notificationController.generateDirectDebitInvalidTokenResponse();
    }

}
