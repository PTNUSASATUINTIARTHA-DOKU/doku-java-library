package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.common.DateUtils;
import com.doku.sdk.dokujavalibrary.common.SignatureUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.token.request.TokenB2B2CRequestDto;
import com.doku.sdk.dokujavalibrary.dto.token.request.TokenB2BRequestDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2B2CResponseDto;
import com.doku.sdk.dokujavalibrary.dto.token.response.TokenB2BResponseDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenBodyDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenDto;
import com.doku.sdk.dokujavalibrary.dto.va.notification.token.NotificationTokenHeaderDto;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;

import static com.doku.sdk.dokujavalibrary.common.RsaKeyUtils.getPrivateKey;
import static com.doku.sdk.dokujavalibrary.common.RsaKeyUtils.getPublicKey;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final DateUtils dateUtils;
    private final ConnectionUtils connectionUtils;
    private final Gson gson;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    public String getTimestamp() {
        return dateUtils.getISO8601StringFromDateUTC(LocalDateTime.now(), dateTimeFormatter);
    }

    public String generateAsymmetricSignature(String privateKey, String clientId, String timestamp) {
        return SignatureUtils.createAsymmetricSignature(clientId, timestamp, privateKey);
    }

    public String generateSymmetricSignature(String httpMethod, String endpointUrl, String accessToken, String requestBody, String timestamp, String secretKey) {
        return SignatureUtils.createSymmetricSignature(httpMethod, endpointUrl, accessToken, requestBody, timestamp, secretKey);
    }

    public TokenB2BRequestDto createTokenB2BRequestDTO() {
        return TokenB2BRequestDto.builder()
                .grantType("client_credentials")
                .build();
    }

    public TokenB2BResponseDto createTokenB2B(TokenB2BRequestDto tokenB2BRequestDTO, String timestamp, String clientId, String signature, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, timestamp);
        httpHeaders.set(SnapHeaderConstant.X_CLIENT_KEY, clientId);
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, signature);

        String url = SdkConfig.getAccessTokenUrl(isProduction);

        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(tokenB2BRequestDTO));

        TokenB2BResponseDto tokenB2BResponseDto = gson.fromJson(response.getBody(), TokenB2BResponseDto.class);
        long expiresIn = 890;
        if (Strings.isNotEmpty(tokenB2BResponseDto.getExpiresIn())) {
            expiresIn = Long.parseLong(tokenB2BResponseDto.getExpiresIn()) - 10;
        }
        tokenB2BResponseDto.setExpiresIn(String.valueOf(expiresIn));

        return tokenB2BResponseDto;
    }

    public Boolean isTokenEmpty(String tokenB2b) {
        return tokenB2b == null;
    }

    public Boolean isTokenExpired(long tokenExpiresIn, long tokenGeneratedTimestamp) {
        var expiryTime = tokenExpiresIn + tokenGeneratedTimestamp;
        var currentTime = Instant.now().getEpochSecond();

        return expiryTime < currentTime;
    }

    public Boolean compareSignatures(String clientId, String timestamp, String signature, String publicKey) {
        StringBuilder strToSign = new StringBuilder();
        strToSign.append(clientId).append("|");
        strToSign.append(timestamp);

        return SignatureUtils.verifySignatureAes256WithRsa(strToSign.toString(), signature, publicKey);
    }

    @SneakyThrows
    public String generateToken(long expiredIn, String issuer, String clientId, String privateKey) {
        return Jwts.builder()
                .setExpiration(Date.from(Instant.now().plus(900, ChronoUnit.SECONDS)))
                .setIssuer(issuer)
                .claim("clientId", clientId)
                .signWith(getPrivateKey(privateKey), SignatureAlgorithm.RS256).compact();
    }

    public NotificationTokenDto generateNotificationTokenDto(String token, String timestamp, String clientId, long expiresIn) {
        return NotificationTokenDto.builder()
                .header(NotificationTokenHeaderDto.builder()
                        .xClientKey(clientId)
                        .xTimestamp(timestamp)
                        .build())
                .body(NotificationTokenBodyDto.builder()
                        .responseCode("2007300")
                        .responseMessage("Successful")
                        .accessToken(token)
                        .tokenType("Bearer")
                        .expiresIn(String.valueOf(expiresIn))
                        .additionalInfo("")
                        .build())
                .build();
    }

    public NotificationTokenDto generateInvalidSignature(String timestamp) {
        return NotificationTokenDto.builder()
                .header(NotificationTokenHeaderDto.builder()
                        .xTimestamp(timestamp)
                        .build())
                .body(NotificationTokenBodyDto.builder()
                        .responseCode("4017300")
                        .responseMessage("Unauthorized. Invalid Signature")
                        .build())
                .build();
    }

    public Boolean validateToken(String requestToken, String publicKey) {
        try {
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(getPublicKey(publicKey))
                    .build()
                    .parseClaimsJws(requestToken);
            log.debug("token valid");
            return true;
        } catch (Exception e) {
            log.debug("token invalid. {}", e.getMessage());
            return false;
        }
    }

    public TokenB2B2CRequestDto createTokenB2b2cRequestDto(String authCode) {
        return TokenB2B2CRequestDto.builder()
                .grantType("authorization_code")
                .authCode(authCode)
                .build();
    }

    public TokenB2B2CResponseDto hitTokenB2b2cApi(TokenB2B2CRequestDto tokenB2B2CRequestDto,
                                                  String timestamp,
                                                  String signature,
                                                  String clientId,
                                                  boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, timestamp);
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, signature);
        httpHeaders.set(SnapHeaderConstant.X_CLIENT_KEY, clientId);

        String url = SdkConfig.getAccessTokenB2b2cUrl(isProduction);

        var response = connectionUtils.httpPost(url, httpHeaders, gson.toJson(tokenB2B2CRequestDto));
        TokenB2B2CResponseDto tokenB2B2CResponseDto = gson.fromJson(response.getBody(), TokenB2B2CResponseDto.class);

        return tokenB2B2CResponseDto;
    }
}
