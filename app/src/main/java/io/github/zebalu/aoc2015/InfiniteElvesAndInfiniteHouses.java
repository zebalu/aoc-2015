package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InfiniteElvesAndInfiniteHouses {
    public static void main(String[] args) {
        int input = readInput();
        firstPart(input);
        secondPart(input);
    }

    private static void firstPart(int input) {
        System.out.println(findFirstHouse(input, 10, InfiniteElvesAndInfiniteHouses::presentsOf));
    }

    private static void secondPart(int input) {
        System.out.println(findFirstHouse(input, 11, InfiniteElvesAndInfiniteHouses::lazyPresentsOf));
    }

    private static int findFirstHouse(int input, int firstScore, ToIntFunction<Integer> scoreCounter) {
        return IntStream.iterate(1, a -> a + 1).parallel().filter(i -> scoreCounter.applyAsInt(i) >= input).findFirst()
                .orElseThrow();
    }

    private static int lazyPresentsOf(int house) {
        return divisors(house).stream().mapToInt(Integer::intValue).filter(i -> house / i <= 50).sum() * 11;
    }

    private static int presentsOf(int house) {
        return divisors(house).stream().mapToInt(Integer::intValue).sum() * 10;
    }

    private static Set<Integer> divisors(int num) {
        return IntStream.rangeClosed(1, (int) Math.sqrt(num)).filter(i -> num % i == 0)
                .mapToObj(i -> List.of(i, num / i)).flatMap(List::stream).collect(Collectors.toSet());

    }

    private static int readInput() {
        try (var br = new BufferedReader(
                new InputStreamReader(InfiniteElvesAndInfiniteHouses.class.getResourceAsStream("/day20.txt")))) {
            return Integer.parseInt(br.lines().collect(Collectors.joining()).trim());
        } catch (Exception e) {
            return Integer.parseInt(INPUT);
        }
    }

    private static final String INPUT = "36000000";
}
