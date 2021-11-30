package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public class CorporatePolicy {
    private static final int BASE = Character.getNumericValue('a');
    private static final int TOP = Character.getNumericValue('z');
    private static final int RANGE = TOP - BASE + 1;
    private static final Set<Byte> FORBIDDEN = Set.of(
            Byte.valueOf((byte) (Character.getNumericValue('l') - BASE)),
            Byte.valueOf((byte) (Character.getNumericValue('o') - BASE)),
            Byte.valueOf((byte) (Character.getNumericValue('i') - BASE))
            );
    
    public static void main(String[] args) {
        secondPart(firstPart());
    }


    private static String firstPart() {
        String next = nextPassword(readInput());
        System.out.println(next);
        return next;
    }
    
    private static void secondPart(String next) {
        String nextNext = nextPassword(increment(next));
        System.out.println(nextNext);
    }
    
    private static String nextPassword(String initialPassword) {
        byte[] encoded = encode(initialPassword);
        while (!fitsAllRequirements(encoded)) {
            increment(encoded);
        }
        return decode(encoded);
    }

    private static void increment(byte[] password) {
        byte inc = (byte) 1;
        for (int i = password.length - 1; i >= 0 && inc > (byte) 0; --i) {
            byte inced = (byte) (password[i] + inc);
            password[i] = (byte) (inced % RANGE);
            inc = (byte) (inced / RANGE);
        }
    }
    
    private static String increment(String start) {
        var encoded = encode(start);
        increment(encoded);
        return decode(encoded);
    }

    private static byte[] encode(String password) {
        byte[] encoded = new byte[password.length()];
        for (int i = 0; i < encoded.length; ++i) {
            encoded[i] = (byte) (Character.getNumericValue(password.charAt(i)) - BASE);
        }
        return encoded;
    }

    private static String decode(byte[] encoded) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < encoded.length; ++i) {
            password.append((char) ('a' + encoded[i]));
        }
        return password.toString();
    }

    private static boolean contains3LongIncreasingSequence(byte[] encoded) {
        for (int i = 0; i < encoded.length - 2; ++i) {
            int ei = encoded[i];
            int eii = encoded[i + 1];
            int eiii = encoded[i + 2];
            if (ei == eii - 1 && ei == eiii - 2) {
                return true;
            }
        }
        return false;
    }

    private static boolean fitsAllRequirements(byte[] encoded) {
        return onlyContainsAllowed(encoded) && contains3LongIncreasingSequence(encoded)
                && contains2DoubleLetters(encoded);
    }

    private static boolean onlyContainsAllowed(byte[] encoded) {
        for (int i = 0; i < encoded.length; ++i) {
            if (FORBIDDEN.contains(encoded[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean contains2DoubleLetters(byte[] encoded) {
        int firstStart = startOfDubleLetters(0, encoded);
        if (firstStart < 0) {
            return false;
        }
        int secondStart = startOfDubleLetters(firstStart + 2, encoded);
        if (secondStart > 0) {
            return true;
        }
        return false;
    }

    private static int startOfDubleLetters(int from, byte[] encoded) {
        int i = from;
        while (i < encoded.length - 1) {
            if (encoded[i] == encoded[i + 1]) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    private static String readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(CorporatePolicy.class.getResourceAsStream("/day11.txt")))) {
            return reader.lines().collect(Collectors.joining());
        } catch (Exception e) {
            return INPUT;
        }
    }

    private static final String INPUT = "hepxcrrq";
}
