package com.doku.sdk.dokujavalibrary.config;

public class SdkConfig {

    private static final String SANDBOX_BASE_URL = "https://api-uat.doku.com";
    private static final String PRODUCTION_BASE_URL = "https://dashboard.doku.com";
    private static final String ACCESS_TOKEN_B2B = "/authorization/v1/access-token/b2b";
    private static final String CREATE_VA = "/virtual-accounts/bi-snap-va/v1.1/transfer-va/create-va";
    private static final String UPDATE_VA = "/virtual-accounts/bi-snap-va/v1.1/transfer-va/update-va";
    private static final String DELETE_VA = "/virtual-accounts/bi-snap-va/v1.1/transfer-va/delete-va";
    private static final String CHECK_STATUS_VA = "/orders/v1.0/transfer-va/status";
    private static final String DIRECT_DEBIT_ACCOUNT_BINDING = "/direct-debit/core/v1/registration-account-binding";
    private static final String DIRECT_DEBIT_ACCOUNT_UNBINDING = "/direct-debit/core/v1/registration-account-unbinding";

    public static String getBaseUrl(boolean isProduction) {
        return isProduction ? PRODUCTION_BASE_URL : SANDBOX_BASE_URL;
    }

    public static String getAccessTokenUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + ACCESS_TOKEN_B2B;
    }

    public static String getCreateVaUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + CREATE_VA;
    }

    public static String getUpdateVaUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + UPDATE_VA;
    }

    public static String getDeleteVaUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + DELETE_VA;
    }

    public static String getCheckStatusVaUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + CHECK_STATUS_VA;
    }

    public static String getDirectDebitAccountBindingUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + DIRECT_DEBIT_ACCOUNT_BINDING;
    }

    public static String getDirectDebitAccountUnbindingUrl(boolean isProduction) {
        return getBaseUrl(isProduction) + DIRECT_DEBIT_ACCOUNT_UNBINDING;
    }
}
