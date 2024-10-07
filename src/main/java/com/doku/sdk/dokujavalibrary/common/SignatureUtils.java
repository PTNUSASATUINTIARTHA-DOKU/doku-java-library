package com.doku.sdk.dokujavalibrary.common;

import com.doku.sdk.dokujavalibrary.exception.GeneralException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.doku.sdk.dokujavalibrary.common.RsaKeyUtils.getPrivateKey;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

@Slf4j
public class SignatureUtils {

    private SignatureUtils() {
    }

    @SneakyThrows
    public static String createAsymmetricSignature(String clientId, String timestamp, String privateKey) {
        StringBuilder component = new StringBuilder();
        component.append(clientId).append("|");
        component.append(timestamp);
        log.debug("stringToSign: {}", component);
        String hashResult = SHA256withRSA(component.toString(), getPrivateKey(privateKey));
        log.debug("asymmetric signature: {}", hashResult);

        return hashResult;
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
        log.debug("stringToSign: {}", component);
        String hashResult = hmacSHA512(component.toString(), secretKey);
        log.debug("symmetric signature: {}", hashResult);

        return hashResult;
    }

    @SneakyThrows
    public static String sha256HexJsonMinify(String requestBody) {
        log.debug("Expected component json body (payload component): \n {} ", requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readValue(requestBody, JsonNode.class);
        String digestHex = DigestUtils.sha256Hex(jsonNode.toString().getBytes(StandardCharsets.UTF_8));
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

    public static boolean verifySignatureAes256WithRsa(String stringToSign, String signature, String stringPublicKey) {
        boolean isSignatureValid = false;

        log.debug("string public Key in PEM format {}", stringPublicKey);
        stringPublicKey = stringPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        stringPublicKey = stringPublicKey.replace("-----END PUBLIC KEY-----", "");
        stringPublicKey = stringPublicKey.replace("\n", "");
        byte[] encoded = decodeBase64(stringPublicKey);

        KeyFactory kf = null;
        Signature publicSignature = null;
        RSAPublicKey publicKey = null;

        try {
            kf = KeyFactory.getInstance("RSA");
            publicSignature = Signature.getInstance("SHA256withRSA");
            publicKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
            publicSignature.initVerify(publicKey);
            publicSignature.update(stringToSign.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            log.error(e.getMessage());
            throw new GeneralException("", "");
        }

        try {
            byte[] decodedSignature = Base64.getDecoder().decode(signature);
            isSignatureValid = publicSignature.verify(decodedSignature);
            log.debug("verifying signature: {}", isSignatureValid);
            return isSignatureValid;
        } catch (SignatureException | NullPointerException e) {
            log.error(e.getMessage());
            throw new GeneralException("", "Signature Invalid");
        }
    }

    public static boolean verifySignatureHmacSha512(String strToSign, String signatureFromClient, String clientSecret) {
        boolean isSignatureValid = false;

        try {
            log.debug("Expected component signature (header component): \n" + "{} ", strToSign);
            byte[] decodedKey = clientSecret.getBytes();
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
            Mac hmacSha512 = Mac.getInstance("HmacSHA512");
            hmacSha512.init(originalKey);
            hmacSha512.update(strToSign.getBytes());
            byte[] hmacSha256DigestBytes = hmacSha512.doFinal();
            String expectedSignature = Base64.getEncoder().encodeToString(hmacSha256DigestBytes);

            if (!expectedSignature.equalsIgnoreCase(signatureFromClient)) {
                log.debug("Expected Signature   : {}", expectedSignature);
                log.debug("Client Signature     : {}", signatureFromClient);
                throw new GeneralException(HttpStatus.UNAUTHORIZED.name(), "Signature Not Match");
            }

            isSignatureValid = true;

        } catch (NullPointerException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new GeneralException(HttpStatus.UNAUTHORIZED.name(), "Signature Invalid");
        }
        log.debug("verifying signature: {}", isSignatureValid);

        return isSignatureValid;
    }
}
