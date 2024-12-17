package com.karczewski.its.user.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UserPasswordUpdateValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPasswordConstraint {

    String message() default "Passwords creation rules: minimum eight characters, at least one capital letter, at least one digit.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
