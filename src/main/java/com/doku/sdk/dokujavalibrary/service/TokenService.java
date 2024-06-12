package com.doku.sdk.dokujavalibrary.service;

import com.doku.sdk.dokujavalibrary.common.ConnectionUtils;
import com.doku.sdk.dokujavalibrary.common.DateUtils;
import com.doku.sdk.dokujavalibrary.common.SignatureUtils;
import com.doku.sdk.dokujavalibrary.config.SdkConfig;
import com.doku.sdk.dokujavalibrary.constant.SnapHeaderConstant;
import com.doku.sdk.dokujavalibrary.dto.request.TokenB2BRequestDto;
import com.doku.sdk.dokujavalibrary.dto.response.TokenB2BResponseDto;
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

import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

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

    @SneakyThrows
    public String createSignature(PrivateKey privateKey, String clientId, String timestamp) {
        return SignatureUtils.createTokenB2bSignature(clientId, timestamp, privateKey);
    }

    public TokenB2BRequestDto createTokenB2BRequestDTO(String signature, String clientId, String timestamp) {
        return TokenB2BRequestDto.builder()
                .signature(signature)
                .timestamp(timestamp)
                .clientId(clientId)
                .grantType("client_credentials")
                .build();
    }

    @SneakyThrows
    public TokenB2BResponseDto createTokenB2B(TokenB2BRequestDto tokenB2BRequestDTO, Boolean isProduction) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set(SnapHeaderConstant.X_TIMESTAMP, tokenB2BRequestDTO.getTimestamp());
        httpHeaders.set(SnapHeaderConstant.X_CLIENT_KEY, tokenB2BRequestDTO.getClientId());
        httpHeaders.set(SnapHeaderConstant.X_SIGNATURE, tokenB2BRequestDTO.getSignature());

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

    public Boolean compareSignatures(String requestSignature, String newSignature) {
        return requestSignature.matches(newSignature);
    }

    @SneakyThrows
    public String generateToken(String expiredIn, String issuer, String clientId, PrivateKey privateKey) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        var parsedExpiredDate = dateFormat.parse(expiredIn);

        return Jwts.builder()
                .setExpiration(parsedExpiredDate)
                .setIssuer(issuer)
                .claim("clientId", clientId)
                .signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }

    public NotificationTokenDto generateNotificationTokenDto(String token, String timestamp, String clientId, String expiresIn) {
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
                        .expiresIn(expiresIn)
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

    public Boolean validateTokenB2b(String requestTokenB2b, String publicKey) {
        try {
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(requestTokenB2b);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
