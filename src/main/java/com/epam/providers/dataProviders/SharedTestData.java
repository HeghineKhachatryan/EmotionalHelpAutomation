package com.epam.providers.dataProviders;

public final class SharedTestData {

    private SharedTestData() {}
    private static String JWTToken;

    public static String getJWTToken() {
        return JWTToken;
    }

    public static void setJWTToken(String JWTToken) {
        SharedTestData.JWTToken = JWTToken;
    }
}
