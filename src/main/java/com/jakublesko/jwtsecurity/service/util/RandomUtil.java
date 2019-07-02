package com.jakublesko.jwtsecurity.service.util;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.security.SecureRandom;

public final class RandomUtil {

    private static final int KEY_LENGTH = 20;

    private static RandomStringGenerator randomStringGenerator;

    static {
        SecureRandom secureRandom = new SecureRandom();
        randomStringGenerator = new RandomStringGenerator.Builder()
            .usingRandom(secureRandom::nextInt)
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build();
    }

    private RandomUtil() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    public static String generateToken() {
        return randomStringGenerator.generate(KEY_LENGTH);
    }
}
