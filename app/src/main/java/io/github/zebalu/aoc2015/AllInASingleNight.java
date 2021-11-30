package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AllInASingleNight {

    private static final Pattern DISTANCE_PATTERN = Pattern.compile("(\\S+) to (\\S+) = (\\d+)");

    public static void main(String[] args) {
        Map<String, Map<String, Integer>> cityGraph = createCityGraph();
        firstPart(cityGraph);
        secondPart(cityGraph);
    }

    private static void firstPart(Map<String, Map<String, Integer>> cityGraph) {
        Queue<CityPath> queue = new PriorityQueue<>();
        cityGraph.keySet().forEach(city -> queue.add(new CityPath(city)));
        while (!queue.isEmpty() && queue.peek().getSize() < cityGraph.size()) {
            var top = queue.poll();
            top.getNexts(cityGraph.get(top.lastCity())).forEach(queue::add);
        }
        System.out.println(queue.peek().getCost());
    }

    private static void secondPart(Map<String, Map<String, Integer>> cityGraph) {
        Queue<CityPath> queue = new PriorityQueue<>(
                Comparator.comparingInt(CityPath::getSize).thenComparing(CityPath::getCost, Comparator.reverseOrder()));
        cityGraph.keySet().forEach(city -> queue.add(new CityPath(city)));
        while (!queue.isEmpty() && queue.peek().getSize() != cityGraph.size()) {
            var top = queue.poll();
            top.getNexts(cityGraph.get(top.lastCity())).forEach(queue::add);
        }
        System.out.println(queue.peek().getCost());
    }

    private static Map<String, Map<String, Integer>> createCityGraph() {
        Map<String, Map<String, Integer>> cityGraph = new HashMap<>();
        for (String line : readInput()) {
            var matcher = DISTANCE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("can not work with: " + line);
            }
            addToGraph(cityGraph, matcher.group(1), matcher.group(2), Integer.parseInt(matcher.group(3)));
        }
        return cityGraph;
    }

    private static void addToGraph(Map<String, Map<String, Integer>> cityGraph, String city1, String city2, int cost) {
        cityGraph.computeIfAbsent(city1, k -> new HashMap<>()).put(city2, cost);
        cityGraph.computeIfAbsent(city2, k -> new HashMap<>()).put(city1, cost);
    }

    private static class CityPath implements Comparable<CityPath> {
        private static final Comparator<CityPath> CITY_PATH_COMPARATOR = Comparator.comparingInt(CityPath::getCost);

        private final List<String> steps = new ArrayList<>();
        private final Set<String> seen = new HashSet<>();
        private final int cost;

        CityPath(String start) {
            steps.add(start);
            seen.add(start);
            cost = 0;
        }

        private CityPath(String next, int cost, CityPath history) {
            if (history.contains(next)) {
                throw new IllegalArgumentException("city: " + next + " is already visited");
            }
            steps.addAll(history.steps);
            seen.addAll(history.seen);
            steps.add(next);
            seen.add(next);
            this.cost = cost + history.cost;
        }

        boolean contains(String city) {
            return seen.contains(city);
        }

        int getCost() {
            return cost;
        }

        int getSize() {
            return steps.size();
        }

        String lastCity() {
            return steps.get(steps.size() - 1);
        }

        List<CityPath> getNexts(Map<String, Integer> availables) {
            return availables.entrySet().stream().filter(e -> !contains(e.getKey()))
                    .map(e -> new CityPath(e.getKey(), e.getValue(), this)).collect(Collectors.toList());
        }

        @Override
        public int compareTo(CityPath other) {
            return CITY_PATH_COMPARATOR.compare(this, other);
        }
    }

    private static final List<String> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(AllInASingleNight.class.getResourceAsStream("/day09.txt")))) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return INPUT.lines().collect(Collectors.toList());
        }
    }

    private static final String INPUT = """
            AlphaCentauri to Snowdin = 66
            AlphaCentauri to Tambi = 28
            AlphaCentauri to Faerun = 60
            AlphaCentauri to Norrath = 34
            AlphaCentauri to Straylight = 34
            AlphaCentauri to Tristram = 3
            AlphaCentauri to Arbre = 108
            Snowdin to Tambi = 22
            Snowdin to Faerun = 12
            Snowdin to Norrath = 91
            Snowdin to Straylight = 121
            Snowdin to Tristram = 111
            Snowdin to Arbre = 71
            Tambi to Faerun = 39
            Tambi to Norrath = 113
            Tambi to Straylight = 130
            Tambi to Tristram = 35
            Tambi to Arbre = 40
            Faerun to Norrath = 63
            Faerun to Straylight = 21
            Faerun to Tristram = 57
            Faerun to Arbre = 83
            Norrath to Straylight = 9
            Norrath to Tristram = 50
            Norrath to Arbre = 60
            Straylight to Tristram = 27
            Straylight to Arbre = 81
            Tristram to Arbre = 90""";
}
