package com.doku.sdk.dokujavalibrary.config;

public class SdkConfig {

    private static final String SANDBOX_BASE_URL = "https://api-uat.doku.com";
    private static final String PRODUCTION_BASE_URL = "https://dashboard.doku.com";
    private static final String ACCESS_TOKEN_B2B = "/authorization/v1/access-token/b2b";
    private static final String CREATE_VA = "/virtual-accounts/bi-snap-va/v1/transfer-va/create-va";

    public static String getBaseUrl(boolean isProduction) {
        return isProduction ? PRODUCTION_BASE_URL : SANDBOX_BASE_URL;
    }

    public static String getAccessTokenUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + ACCESS_TOKEN_B2B;
    }

    public static String getCreateVaUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + CREATE_VA;
    }
}
