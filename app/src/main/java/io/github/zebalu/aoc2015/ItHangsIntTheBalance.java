package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class ItHangsIntTheBalance {
    public static void main(String[] args) {
        var nums = readInput();
        firstPart(nums);
        secondPart(nums);
    }

    private static void firstPart(Set<Integer> nums) {
        int target = nums.stream().mapToInt(Integer::intValue).sum() / 3;
        long min = smallestBags(nums, target).stream().mapToLong(Bag::quantumEntanglement).min().orElseThrow();
        System.out.println(min);
    }

    private static void secondPart(Set<Integer> nums) {
        int target = nums.stream().mapToInt(Integer::intValue).sum() / 4;
        long min = smallestBags(nums, target).stream().mapToLong(Bag::quantumEntanglement).min().orElseThrow();
        System.out.println(min);
    }

    private static List<Bag> smallestBags(Set<Integer> numSet, int target) {
        Set<Bag> collector = new HashSet<>();
        int maxSize = Integer.MAX_VALUE;
        Queue<Step> queue = new PriorityQueue<>(Comparator.comparing(s -> s.to().weight, Comparator.reverseOrder()));
        queue.add(createStartStep(numSet));
        boolean allFound = false;
        while (!queue.isEmpty() && !allFound) {
            Step top = queue.poll();
            if (top.to.presents.size() > maxSize) {
                allFound = true;
            } else {
                if (top.to().weight == target) {
                    if (maxSize > top.to().presents.size()) {
                        maxSize = top.to().presents.size();
                        collector.clear();
                    }
                    collector.add(top.to());
                } else if (top.to().presents.size() < maxSize) {
                    for (int present : top.from().presents) {
                        if (top.to.weight + present <= target) {
                            queue.add(new Step(top.from().remove(present), top.to().extend(present)));
                        }
                    }
                }
            }
        }
        return new ArrayList<>(collector);
    }

    private static Step createStartStep(Set<Integer> numSet) {
        Bag from = new Bag();
        from.fill(numSet);
        Step startStep = new Step(from, new Bag());
        return startStep;
    }

    private static class Bag {
        List<Integer> presents = new LinkedList<>();
        int weight = 0;

        Bag extend(int present) {
            Bag result = new Bag();
            result.presents.addAll(presents);
            result.presents.add(present);
            result.presents.sort(Comparator.reverseOrder());
            result.weight = weight + present;
            return result;
        }

        Bag remove(int present) {
            Bag result = new Bag();
            int idx = presents.indexOf(present);
            result.presents.addAll(presents.subList(idx + 1, presents.size()));
            result.weight = weight - present;
            return result;
        }

        void fill(Set<Integer> presents) {
            this.presents.addAll(presents.stream().sorted(Comparator.reverseOrder()).toList());
            weight = this.presents.stream().mapToInt(Integer::intValue).sum();
        }

        long quantumEntanglement() {
            return presents.stream().mapToLong(Integer::longValue).reduce(1L, (a, v) -> a * v);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Bag b) {
                if (presents.size() == b.presents.size()) {
                    for (int i = 0; i < presents.size(); ++i) {
                        if (presents.get(i) != b.presents.get(i)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return presents.stream().mapToInt(Integer::intValue).reduce(1, (a, v) -> a * v);
        }

        @Override
        public String toString() {
            return presents.toString();
        }
    }

    private static record Step(Bag from, Bag to) {
    }

    private static Set<Integer> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(ItHangsIntTheBalance.class.getResourceAsStream("/day24.txt")))) {
            return reader.lines().map(Integer::parseInt).collect(Collectors.toSet());
        } catch (Exception e) {
            return INPUT.lines().map(Integer::parseInt).collect(Collectors.toSet());
        }
    }

    private static final String INPUT = """
            1
            2
            3
            5
            7
            13
            17
            19
            23
            29
            31
            37
            41
            43
            53
            59
            61
            67
            71
            73
            79
            83
            89
            97
            101
            103
            107
            109
            113""";
}
