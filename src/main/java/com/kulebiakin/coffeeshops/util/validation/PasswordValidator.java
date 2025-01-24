package com.kulebiakin.coffeeshops.util.validation;

public class PasswordValidator {
    private static final int PASSWORD_MINIMUM_LENGTH = 3;

    public static boolean isValid(String password) {
        return password != null && password.length() >= PASSWORD_MINIMUM_LENGTH;
    }
}
