package com.karczewski.its.user.component;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class UserPasswordGenerateComponent {

    private static final Random RANDOM = new SecureRandom();

    String generatePassword(PasswordRules rules) {
        List<Character> charList = Stream
                .concat(getRandomSpecialCharacters(rules.amountOfSpecialChars()),
                        Stream.concat(getRandomNumbers(rules.amountOfNumbers()), Stream.concat(
                                getUppercaseLetter(rules.amountOfUppercaseLetters()),
                                getLowercaseLetter(rules.amountOfLowercaseLetters()))))
                .collect(toList());
        Collections.shuffle(charList);
        return charList.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private Stream<Character> getRandomSpecialCharacters(int amount) {
        IntStream integers = RANDOM.ints(amount, 33, 45);
        return integers.mapToObj(i -> (char) i);
    }

    private Stream<Character> getRandomNumbers(int amount) {
        IntStream integers = RANDOM.ints(amount, 48, 58);
        return integers.mapToObj(i -> (char) i);
    }

    private Stream<Character> getUppercaseLetter(int amount) {
        IntStream integers = RANDOM.ints(amount, 65, 91);
        return integers.mapToObj(i -> (char) i);
    }

    private Stream<Character> getLowercaseLetter(int amount) {
        IntStream integers = RANDOM.ints(amount, 97, 123);
        return integers.mapToObj(i -> (char) i);
    }

}
