package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

public class TheIdealStockingStuffer {

    private static final MessageDigest MD5;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Can not instantiate MD5", e);
        }
    }

    private static final String KEY = readInput();

    public static void main(String[] args) {
        firstPart();
        secondPart();
    }

    private static void firstPart() {
        int i = 0;
        while (!startsWith(KEY, i, "00000")) {
            ++i;
        }
        System.out.println(i);
    }

    private static void secondPart() {
        int i = 0;
        while (!startsWith(KEY, i, "000000")) {
            ++i;
        }
        System.out.println(i);
    }

    private static boolean startsWith(String key, int i, String target) {
        return toMd5String(key + i).startsWith(target);
    }

    private static String toMd5String(String combined) {
        return String.format("%032x", new BigInteger(1, MD5.digest(combined.getBytes(StandardCharsets.UTF_8))));
    }

    private static final String readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(TheIdealStockingStuffer.class.getResourceAsStream("/day04.txt")))) {
            return reader.lines().collect(Collectors.joining()).trim();
        } catch (Exception e) {
            return INPUT;
        }
    }

    private static final String INPUT = "ckczppom";
}
