package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class ThereIsNoSuchThingAsTooMuch {
    public static void main(String[] args) {
        var containers = readInput();
        firstPart(containers);
        secondPart(containers);
    }

    private static void firstPart(List<Integer> containers) {
        var ways = countWays(containers, 0, 150, 0);
        System.out.println(ways);
    }

    private static void secondPart(List<Integer> containers) {
        var minContainerRequirement = findMinimumContainerCount(containers, 0, 150, 0);
        System.out.println(countWaysWithLimitedContainers(containers, 0, 150, 0, minContainerRequirement));
    }

    private static int countWays(List<Integer> containers, int startIndex, int target, int countSoFar) {
        if (target == 0) {
            return 1;
        } else if (target < 0) {
            return 0;
        } else {
            int count = countSoFar;
            for (int i = startIndex; i < containers.size(); ++i) {
                count += countWays(containers, i + 1, target - containers.get(i), countSoFar);
            }
            return count;
        }
    }

    private static int findMinimumContainerCount(List<Integer> containers, int startIndex, int target,
            int containerCount) {
        if (target == 0) {
            return containerCount;
        } else if (target < 0) {
            return Integer.MAX_VALUE;
        } else {
            int min = Integer.MAX_VALUE;
            for (int i = startIndex; i < containers.size(); ++i) {
                min = Math.min(min,
                        findMinimumContainerCount(containers, i + 1, target - containers.get(i), containerCount + 1));
            }
            return min;
        }
    }

    private static int countWaysWithLimitedContainers(List<Integer> containers, int startIndex, int target,
            int countSoFar, int usableContainers) {
        if (target == 0 && usableContainers >= 0) {
            return 1;
        } else if (target < 0 || usableContainers < 0) {
            return 0;
        } else {
            int count = countSoFar;
            for (int i = startIndex; i < containers.size(); ++i) {
                count += countWaysWithLimitedContainers(containers, i + 1, target - containers.get(i), countSoFar,
                        usableContainers - 1);
            }
            return count;
        }
    }

    private static final List<Integer> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(ThereIsNoSuchThingAsTooMuch.class.getResourceAsStream("/day17.txt")))) {
            return reader.lines().map(Integer::parseInt).toList();
        } catch (Exception e) {
            return INPUT.lines().map(Integer::parseInt).toList();
        }
    }

    private static final String INPUT = """
            50
            44
            11
            49
            42
            46
            18
            32
            26
            40
            21
            7
            18
            43
            10
            47
            36
            24
            22
            40""";
}
