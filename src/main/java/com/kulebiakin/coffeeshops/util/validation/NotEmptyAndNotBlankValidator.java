package com.kulebiakin.coffeeshops.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyAndNotBlankValidator implements ConstraintValidator<NotEmptyAndNotBlank, String> {

    private String emptyMessage;
    private String blankMessage;

    @Override
    public void initialize(NotEmptyAndNotBlank constraintAnnotation) {
        this.emptyMessage = constraintAnnotation.emptyMessage();
        this.blankMessage = constraintAnnotation.blankMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            // Disable default violation message
            context.disableDefaultConstraintViolation();
            // Add custom violation message
            context.buildConstraintViolationWithTemplate(emptyMessage).addConstraintViolation();
            return false;
        }

        if (value.trim().isEmpty()) {
            // Disable default violation message
            context.disableDefaultConstraintViolation();
            // Add custom violation message
            context.buildConstraintViolationWithTemplate(blankMessage).addConstraintViolation();
            return false;
        }

        return true;
    }
}
