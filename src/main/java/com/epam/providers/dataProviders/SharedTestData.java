package com.epam.providers.dataProviders;

public final class SharedTestData {

    private SharedTestData() {}
    private static String JWTToken;
    private static String password;

    public static String getJWTToken() {
        return JWTToken;
    }

    public static void setJWTToken(String JWTToken) {
        SharedTestData.JWTToken = JWTToken;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SharedTestData.password = password;
    }
}
