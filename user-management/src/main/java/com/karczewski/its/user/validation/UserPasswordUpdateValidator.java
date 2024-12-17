package com.karczewski.its.user.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserPasswordUpdateValidator implements
        ConstraintValidator<UserPasswordConstraint, String> {

    @Override
    public void initialize(UserPasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext constraintValidatorContext) {
        return password.length() >= 8 &&
                containsUpperCase(password) &&
                containsLowerCase(password) &&
                containsDigit(password) &&
                containsSymbol(password);
    }

    private static boolean containsUpperCase(final String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }

    private static boolean containsLowerCase(final String password) {
        return password.chars().anyMatch(Character::isLowerCase);
    }

    private static boolean containsDigit(final String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

    private static boolean containsSymbol(final String password) {
        return password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
    }
}