package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TheIdealStockingStuffer {

    private static final ThreadLocal<MessageDigest> TL_MD5 = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    });

    private static final String KEY = readInput();

    public static void main(String[] args) {
        firstPart();
        secondPart();
    }

    private static void firstPart() {
        System.out.println(parallelFirstWithStart("00000"));
    }

    private static void secondPart() {
        System.out.println(parallelFirstWithStart("000000"));
    }

    private static int parallelFirstWithStart(String start) {
        return IntStream.iterate(0, i -> i + 1).parallel().filter(i -> startsWith(KEY, i, start)).findFirst()
                .orElseThrow();
    }

    private static boolean startsWith(String key, int i, String target) {
        return toMd5String(key + i).startsWith(target);
    }

    private static String toMd5String(String combined) {
        return String.format("%032x",
                new BigInteger(1, TL_MD5.get().digest(combined.getBytes(StandardCharsets.UTF_8))));
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
