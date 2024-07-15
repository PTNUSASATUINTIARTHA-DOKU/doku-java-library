package com.doku.sdk.dokujavalibrary.common;

import lombok.SneakyThrows;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaKeyUtils {

    @SneakyThrows
    public static PublicKey getPublicKey(String publicKey) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactoryPublic = KeyFactory.getInstance("RSA");
        return keyFactoryPublic.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    @SneakyThrows
    public static PrivateKey getPrivateKey (String privateKey) {
        String keyString = privateKey;
        keyString = keyString.replace("-----BEGIN PRIVATE KEY-----\n", "");
        keyString = keyString.replace("-----END PRIVATE KEY-----", "");

        byte[] decodedPrivateKey = org.apache.commons.codec.binary.Base64.decodeBase64(keyString);
        KeyFactory keyFactoryPrivate = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decodedPrivateKey);

        return keyFactoryPrivate.generatePrivate(keySpecPrivate);
    }
}
