package com.petrenko.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class RandomGenerator {
    private static final Random RANDOM = new Random();

    private static int numberOfCharacters = 5;
    private static int maxRandomNumber = 100;

    public static String randomText(final int numberOfCharacters) {
        String abc = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < numberOfCharacters; i++) {
            sb.append(abc.charAt(RANDOM.nextInt(abc.length() - 1)));
        }
        return sb.toString();
    }

    public static String randomText() {
        return randomText(numberOfCharacters);
    }

    public static int randomNumber(int maxRandomNumber) {
        return RANDOM.nextInt(maxRandomNumber + 1);
    }

    public static int randomNumber() {
        return randomNumber(maxRandomNumber);
    }
}
