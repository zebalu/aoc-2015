package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;

public class ScienceForHungryPeople {
    public static void main(String[] args) {
        var ingredients = readInput();
        var distributions = cutToPieces(100, ingredients.size());
        firstPart(ingredients, distributions);
        secondPart(ingredients, distributions);
    }

    private static void firstPart(List<Ingredient> ingredients, List<int[]> distributions) {
        int maxScore = distributions.stream().mapToInt(d -> scoreOfDistribution(d, ingredients)).max().orElseThrow();
        System.out.println(maxScore);
    }

    private static void secondPart(List<Ingredient> ingredients, List<int[]> distributions) {
        int maxScore = distributions.stream().mapToInt(d -> scoreOfDistributionWith500Calories(d, ingredients)).max()
                .orElseThrow();
        System.out.println(maxScore);
    }

    private static int scoreOfDistribution(int[] distribution, List<Ingredient> ingredients) {
        int capacity = mix(distribution, ingredients, Ingredient::capacity);
        int durability = mix(distribution, ingredients, Ingredient::durability);
        int flavour = mix(distribution, ingredients, Ingredient::flavour);
        int texture = mix(distribution, ingredients, Ingredient::texture);
        return capacity * durability * flavour * texture;
    }

    private static int scoreOfDistributionWith500Calories(int[] distribution, List<Ingredient> ingredients) {
        if (mix(distribution, ingredients, Ingredient::calories) != 500) {
            return 0;
        }
        return scoreOfDistribution(distribution, ingredients);
    }

    private static int mix(int[] distribution, List<Ingredient> ingredients, ToIntFunction<Ingredient> getValue) {
        int sum = 0;
        for (int i = 0; i < distribution.length; ++i) {
            sum += distribution[i] * getValue.applyAsInt(ingredients.get(i));
        }
        return Math.max(sum, 0);
    }

    private static List<int[]> cutToPieces(int number, int maxPieces) {
        int[] pieces = new int[maxPieces];
        List<int[]> result = new ArrayList<>();
        cutToPieces(pieces, 0, number, result);
        return result;
    }

    private static void cutToPieces(int[] pieces, int index, int reduced, List<int[]> result) {
        if (reduced == 0) {
            result.add(Arrays.copyOf(pieces, pieces.length));
        } else {
            if (index == pieces.length - 1) {
                pieces[index] = reduced;
                result.add(Arrays.copyOf(pieces, pieces.length));
            } else {
                for (int i = 0; i <= reduced; ++i) {
                    pieces[index] = i;
                    cutToPieces(pieces, index + 1, reduced - i, result);
                }
            }
            pieces[index] = 0;
        }
    }

    private static record Ingredient(String name, int capacity, int durability, int flavour, int texture,
            int calories) {

        private static final Pattern INGREDIENT_PATTERN = Pattern.compile(
                "^([^:]+): capacity ([^,]+), durability ([^,]+), flavor ([^,]+), texture ([^,]+), calories (.+)$");

        static Ingredient parse(String line) {
            var m = INGREDIENT_PATTERN.matcher(line);
            if (!m.matches()) {
                throw new IllegalArgumentException("can't match: '" + line + "'");
            }
            return new Ingredient(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)));
        }
    }

    private static List<Ingredient> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(ScienceForHungryPeople.class.getResourceAsStream("/day15.txt")))) {
            return reader.lines().map(Ingredient::parse).toList();
        } catch (Exception e) {
            return INPUT.lines().map(Ingredient::parse).toList();
        }
    }

    private static final String INPUT = """
            Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2
            Sprinkles: capacity -3, durability 3, flavor 0, texture 0, calories 9
            Candy: capacity -1, durability 0, flavor 4, texture 0, calories 1
            Chocolate: capacity 0, durability 0, flavor -2, texture 2, calories 8""";
}
