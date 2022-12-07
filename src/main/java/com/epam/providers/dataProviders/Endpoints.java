package com.epam.providers.dataProviders;

public enum Endpoints {

    SIGN_UP("signup/user"),
    LOGIN("authentication/login"),
    RESET_PASSWORD("authentication/reset-password"),
    RESET_FORGOTTEN_PASSWORD("authentication/reset-forgotten-password"),
    SEND_EMAIL("authentication/send-mail");

    private final String text;

    Endpoints(String endpoint) {
        this.text = endpoint;
    }

    public String getEndpoint() {
        return text;
    }
}
