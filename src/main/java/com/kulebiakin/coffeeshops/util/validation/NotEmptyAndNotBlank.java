package com.kulebiakin.coffeeshops.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotEmptyAndNotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyAndNotBlank {
    String message() default "Поле не может быть пустым или содержать только пробелы";
    String emptyMessage() default "Поле не может быть пустым";
    String blankMessage() default "Поле не может содержать только пробелы";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
