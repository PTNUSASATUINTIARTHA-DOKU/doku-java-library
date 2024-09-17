package com.doku.sdk.dokujavalibrary.common;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaKeyUtils {

    @SneakyThrows
    public static PublicKey getPublicKey(String publicKey) {
        String keyString = publicKey;
        keyString = keyString.replace("-----BEGIN PUBLIC KEY-----\n", "");
        keyString = keyString.replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(keyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    @SneakyThrows
    public static PrivateKey getPrivateKey (String privateKey) {
        Security.addProvider(new BouncyCastleProvider());

        String keyString = privateKey;
        keyString = keyString.replace("-----BEGIN PRIVATE KEY-----\n", "");
        keyString = keyString.replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(keyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }
}
