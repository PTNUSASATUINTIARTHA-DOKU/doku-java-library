package com.doku.sdk.dokujavalibrary.common;

import lombok.SneakyThrows;

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

    public static String SHA256withRSA(String stringToSign, PrivateKey privateKey) throws Exception {
        byte[] signature = signMessageWithAes256WithRsa(stringToSign, privateKey);

        return Base64.getEncoder().encodeToString(signature);
    }

    public static byte[] signMessageWithAes256WithRsa(String message, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes(StandardCharsets.UTF_8));

        return privateSignature.sign();
    }
}
