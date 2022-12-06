package com.epam.providers.dataProviders;

public final class SharedTestData {

    private SharedTestData() {}
    private static String JWTToken;
    private static String tokenType;

    private static String currentPassword;
    private static String currentEmail;

    public static String getJWTToken() {
        return JWTToken;
    }

    public static void setJWTToken(String JWTToken) {
        SharedTestData.JWTToken = JWTToken;
    }

    public static String getTokenType() {
        return tokenType;
    }

    public static void setTokenType(String tokenType) {
        SharedTestData.tokenType = tokenType;
    }

    public static String getCurrentPassword() {
        return currentPassword;
    }

    public static void setCurrentPassword(String currentPassword) {
        SharedTestData.currentPassword = currentPassword;
    }

    public static String getCurrentEmail() {
        return currentEmail;
    }

    public static void setCurrentEmail(String currentEmail) {
        SharedTestData.currentEmail = currentEmail;
    }
}
