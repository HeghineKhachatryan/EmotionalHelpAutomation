package com.epam.providers.dataProviders;

import com.epam.helpers.PropertiesReader;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDataProvider {
    private static final PropertiesReader propertiesPassword = PropertiesReader.getInstance("userPassword.properties");
    private static final PropertiesReader propertiesEmail = PropertiesReader.getInstance("userEmail.properties");

    public static String getExistedEmail() {
        return propertiesEmail.getPropertyByKey("existedUserEmail");
    }

    public static String getExistedPassword() {
        return propertiesPassword.getPropertyByKey("existedUserPassword");
    }
    public static String generateStrongPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(3, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(1, 35, 38, false, false);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String generateName() {
        return RandomStringUtils.random(10, true, false);
    }

    public static String generateEmail() {
        return System.currentTimeMillis() + "@gmail.com";
    }
}
