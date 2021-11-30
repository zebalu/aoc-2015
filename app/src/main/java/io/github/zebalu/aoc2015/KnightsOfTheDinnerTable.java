package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class KnightsOfTheDinnerTable {
    private static final String LOVES = "gain";
    private static final String HATES = "lose";
    private static final Pattern LOVE_PATTERN = Pattern
            .compile("(\\S+) would (lose|gain) (\\d+) happiness units by sitting next to (.+)\\.");

    public static void main(String[] args) {
        LoveGraph loveGraph = readLoveGraph();
        secondPart(firstPart(loveGraph), loveGraph);
    }

    private static Arrengement firstPart(LoveGraph loveGraph) {
        var table = bestArrengement(loveGraph);
        System.out.println(table.loveScore(loveGraph));
        return table;
    }

    private static void secondPart(Arrengement arrengement, LoveGraph loveGraph) {
        loveGraph.extend("ME", 0);
        var bestWithMe = insertBestPosition(arrengement, "ME", loveGraph);
        System.out.println(bestWithMe.loveScore(loveGraph));
    }

    private static LoveGraph readLoveGraph() {
        LoveGraph loveGraph = new LoveGraph();
        readInput().forEach(line -> {
            var m = LOVE_PATTERN.matcher(line);
            if (!m.matches()) {
                throw new IllegalArgumentException("can not match: \"" + line + "\"");
            }
            String lover = m.group(1);
            String loved = m.group(4);
            int love = calculateLove(m.group(3), m.group(2));
            loveGraph.computeIfAbsent(lover, k -> new HashMap<>()).put(loved, love);
        });
        return loveGraph;
    }

    private static int calculateLove(String love, String relationship) {
        return switch (relationship) {
        case LOVES -> Integer.parseInt(love);
        case HATES -> -Integer.parseInt(love);
        default -> throw new IllegalArgumentException("unkown relationship: " + relationship);
        };
    }

    private static final class Arrengement {
        private final List<String> sitting = new CircularArrayList<>();
        private final Set<String> sitters = new HashSet<>();

        Arrengement(String init) {
            sitters.add(init);
            sitting.add(init);
        }

        Arrengement(Arrengement copy, String extension, int position) {
            if (copy.sitters.contains(extension)) {
                throw new IllegalArgumentException("this extension: \"" + extension + "\" is already at the table");
            }
            this.sitters.addAll(copy.sitters);
            this.sitting.addAll(copy.sitting);
            this.sitters.add(extension);
            this.sitting.add(position, extension);
        }

        int size() {
            return sitting.size();
        }

        int loveScore(LoveGraph loveGraph) {
            if (sitting.size() < 2) {
                return 0;
            }
            int sum = 0;
            for (int i = 0; i < sitting.size(); ++i) {
                String left = sitting.get(i - 1);
                String middle = sitting.get(i);
                String right = sitting.get(i + 1);
                sum += loveGraph.get(middle).get(left);
                sum += loveGraph.get(middle).get(right);
            }
            return sum;
        }
    }

    private static final class LoveGraph extends HashMap<String, Map<String, Integer>> {
        private static final long serialVersionUID = 1L;

        void extend(String extraName, int constantLove) {
            if (containsKey(extraName)) {
                throw new IllegalArgumentException(
                        "Can not extend Lovegraph with \"" + extraName + "\"; as it is already in the graph");
            }
            Map<String, Integer> extraLove = new HashMap<>();
            keySet().forEach(key -> {
                get(key).put(extraName, constantLove);
                extraLove.put(key, constantLove);
            });
            put(extraName, extraLove);
        }
    }

    private static final class CircularArrayList<E> extends ArrayList<E> {
        private static final long serialVersionUID = 1L;

        @Override
        public E get(int index) {
            return super.get(Math.floorMod(index, size()));
        };
    }

    private static Arrengement bestArrengement(LoveGraph graph) {
        var names = new ArrayList<>(graph.keySet());
        Arrengement current = new Arrengement(names.get(0));
        for (int i = 1; i < names.size(); ++i) {
            current = findBestPartner(current, graph);
        }
        return current;
    }

    private static Arrengement findBestPartner(Arrengement start, LoveGraph graph) {
        return graph.keySet().stream().filter(k -> !start.sitters.contains(k))
                .map(k -> insertBestPosition(start, k, graph)).max(Comparator.comparingInt(a -> a.loveScore(graph)))
                .orElseThrow();
    }

    private static Arrengement insertBestPosition(Arrengement start, String name, LoveGraph graph) {
        return IntStream.range(0, start.size()).mapToObj(i -> new Arrengement(start, name, i))
                .max(Comparator.comparingInt(a -> a.loveScore(graph))).orElseThrow();
    }

    private static List<String> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(KnightsOfTheDinnerTable.class.getResourceAsStream("/day13.txt")))) {
            return reader.lines().toList();
        } catch (Exception e) {
            return INPUT.lines().toList();
        }
    }

    private static final String INPUT = """
            Alice would lose 57 happiness units by sitting next to Bob.
            Alice would lose 62 happiness units by sitting next to Carol.
            Alice would lose 75 happiness units by sitting next to David.
            Alice would gain 71 happiness units by sitting next to Eric.
            Alice would lose 22 happiness units by sitting next to Frank.
            Alice would lose 23 happiness units by sitting next to George.
            Alice would lose 76 happiness units by sitting next to Mallory.
            Bob would lose 14 happiness units by sitting next to Alice.
            Bob would gain 48 happiness units by sitting next to Carol.
            Bob would gain 89 happiness units by sitting next to David.
            Bob would gain 86 happiness units by sitting next to Eric.
            Bob would lose 2 happiness units by sitting next to Frank.
            Bob would gain 27 happiness units by sitting next to George.
            Bob would gain 19 happiness units by sitting next to Mallory.
            Carol would gain 37 happiness units by sitting next to Alice.
            Carol would gain 45 happiness units by sitting next to Bob.
            Carol would gain 24 happiness units by sitting next to David.
            Carol would gain 5 happiness units by sitting next to Eric.
            Carol would lose 68 happiness units by sitting next to Frank.
            Carol would lose 25 happiness units by sitting next to George.
            Carol would gain 30 happiness units by sitting next to Mallory.
            David would lose 51 happiness units by sitting next to Alice.
            David would gain 34 happiness units by sitting next to Bob.
            David would gain 99 happiness units by sitting next to Carol.
            David would gain 91 happiness units by sitting next to Eric.
            David would lose 38 happiness units by sitting next to Frank.
            David would gain 60 happiness units by sitting next to George.
            David would lose 63 happiness units by sitting next to Mallory.
            Eric would gain 23 happiness units by sitting next to Alice.
            Eric would lose 69 happiness units by sitting next to Bob.
            Eric would lose 33 happiness units by sitting next to Carol.
            Eric would lose 47 happiness units by sitting next to David.
            Eric would gain 75 happiness units by sitting next to Frank.
            Eric would gain 82 happiness units by sitting next to George.
            Eric would gain 13 happiness units by sitting next to Mallory.
            Frank would gain 77 happiness units by sitting next to Alice.
            Frank would gain 27 happiness units by sitting next to Bob.
            Frank would lose 87 happiness units by sitting next to Carol.
            Frank would gain 74 happiness units by sitting next to David.
            Frank would lose 41 happiness units by sitting next to Eric.
            Frank would lose 99 happiness units by sitting next to George.
            Frank would gain 26 happiness units by sitting next to Mallory.
            George would lose 63 happiness units by sitting next to Alice.
            George would lose 51 happiness units by sitting next to Bob.
            George would lose 60 happiness units by sitting next to Carol.
            George would gain 30 happiness units by sitting next to David.
            George would lose 100 happiness units by sitting next to Eric.
            George would lose 63 happiness units by sitting next to Frank.
            George would gain 57 happiness units by sitting next to Mallory.
            Mallory would lose 71 happiness units by sitting next to Alice.
            Mallory would lose 28 happiness units by sitting next to Bob.
            Mallory would lose 10 happiness units by sitting next to Carol.
            Mallory would gain 44 happiness units by sitting next to David.
            Mallory would gain 22 happiness units by sitting next to Eric.
            Mallory would gain 79 happiness units by sitting next to Frank.
            Mallory would lose 16 happiness units by sitting next to George.""";

}
