package com.eating.base.util;

import java.security.SecureRandom;

/**
 * @Author han bin
 **/
public class Random {
    private final static char[] CHAR_NUM_10 = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final static char[] CHAR_CAP_26 = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private final static char[] CHAR_LOW_26 = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private final static char[] CHAR_C_52 = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static final char[] CHAR_C_56 = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final static char[] CHAR_C_62 =
            new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                    'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final static SecureRandom RANDOM = new SecureRandom();

    static {
        RANDOM.setSeed(System.currentTimeMillis());
    }

    private Random() {
    }

    public static String getRandom(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_C_62[RANDOM.nextInt(62)]);
        }
        return result.toString();
    }

    public static String getRandomNum(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_NUM_10[RANDOM.nextInt(10)]);
        }
        return result.toString();
    }

    public static String getRandomChar(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_C_52[RANDOM.nextInt(52)]);
        }
        return result.toString();
    }

    public static String getRandomCharLow(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_LOW_26[RANDOM.nextInt(26)]);
        }
        return result.toString();
    }

    public static String getRandomCharCap(int size) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(CHAR_CAP_26[RANDOM.nextInt(26)]);
        }
        return result.toString();
    }

    public static String getRandomRec(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_C_56[random.nextInt(56)]);
        }
        return result.toString();
    }
}
