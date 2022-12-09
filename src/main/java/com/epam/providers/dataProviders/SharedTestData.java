package com.epam.providers.dataProviders;

import lombok.Getter;
import lombok.Setter;

public final class SharedTestData {

    private SharedTestData() {}

    @Getter
    @Setter
    private static String JWTToken;
    @Getter
    @Setter
    private static String password;
    @Getter
    @Setter
    private static String messageText;
    @Getter
    @Setter
    private static int userID;
}
