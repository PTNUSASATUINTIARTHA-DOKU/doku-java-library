package com.doku.sdk.dokujavalibrary.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class SignatureUtils {

    private SignatureUtils() {
    }

    @SneakyThrows
    public static String createTokenB2bSignature(String clientId, String timestamp, PrivateKey privateKey) {
        StringBuilder component = new StringBuilder();
        component.append(clientId).append("|");
        component.append(timestamp);

        return SHA256withRSA(component.toString(), privateKey);
    }

    private static String SHA256withRSA(String stringToSign, PrivateKey privateKey) throws Exception {
        byte[] signature = signMessageWithAes256WithRsa(stringToSign, privateKey);

        return Base64.getEncoder().encodeToString(signature);
    }

    private static byte[] signMessageWithAes256WithRsa(String message, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes(StandardCharsets.UTF_8));

        return privateSignature.sign();
    }

    @SneakyThrows
    public static String createSymmetricSignature(String httpMethod, String endpointUrl, String accessToken, String requestBody, String timestamp, String secretKey) {
        StringBuilder component = new StringBuilder();
        component.append(httpMethod).append(":");
        component.append(endpointUrl).append(":");
        component.append(accessToken).append(":");
        component.append(sha256HexJsonMinify(requestBody)).append(":");
        component.append(timestamp);

        return hmacSHA512(component.toString(), secretKey);
    }

    @SneakyThrows
    private static String sha256HexJsonMinify(String requestBody) {
        var bodyString = requestBody;
        if (Strings.isNotEmpty(requestBody)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readValue(requestBody, JsonNode.class);
            bodyString = jsonNode.toString();
        }
        String digestHex = DigestUtils.sha256Hex(bodyString.getBytes(StandardCharsets.UTF_8));
        return digestHex.toLowerCase();
    }

    private static String hmacSHA512(String stringToSign, String secret) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] decodedKey = secret.getBytes();
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        hmacSha512.init(originalKey);
        hmacSha512.update(stringToSign.getBytes());
        byte[] hmacSha256DigestBytes = hmacSha512.doFinal();
        return Base64.getEncoder().encodeToString(hmacSha256DigestBytes);
    }
}
