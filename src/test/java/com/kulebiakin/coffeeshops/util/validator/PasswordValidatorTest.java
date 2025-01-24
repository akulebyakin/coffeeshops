package com.kulebiakin.coffeeshops.util.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {

    @Test
    void isValid_returnsTrue_whenPasswordIsValid() {
        assertTrue(PasswordValidator.isValid("validPassword"));
    }

    @Test
    void isValid_returnsFalse_whenPasswordIsNull() {
        assertFalse(PasswordValidator.isValid(null));
    }

    @Test
    void isValid_returnsFalse_whenPasswordIsTooShort() {
        assertFalse(PasswordValidator.isValid("ab"));
    }

    @Test
    void isValid_returnsTrue_whenPasswordIsExactlyMinimumLength() {
        assertTrue(PasswordValidator.isValid("abc"));
    }
}
