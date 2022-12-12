package com.epam.providers.dataProviders;

public enum Endpoints {

    SIGN_UP("signup/user"),
    LOGIN("authentication/login"),
    RESET_PASSWORD("authentication/reset-password"),
    RESET_FORGOTTEN_PASSWORD("authentication/reset-forgotten-password"),
    SEND_MAIL("authentication/send-verification-code-to-mail"),
    QUESTIONNAIRES("questionnaires"),
    CONFIRM_EMAIL("signup/confirm-email/"),
    SPECIALISTS("specialists"),
    PROJECTS("projects"),
    ARTICLES("articles"),
    USERS_ID_UPDATE("users/{id}");

    public final String url;

    Endpoints(String endpoint) {
        this.url = endpoint;
    }
}
