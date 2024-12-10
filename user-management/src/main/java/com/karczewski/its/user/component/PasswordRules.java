package com.karczewski.its.user.component;

import lombok.Builder;

@Builder
public record PasswordRules(int amountOfSpecialChars,
                            int amountOfNumbers,
                            int amountOfUppercaseLetters,
                            int amountOfLowercaseLetters) {
}
