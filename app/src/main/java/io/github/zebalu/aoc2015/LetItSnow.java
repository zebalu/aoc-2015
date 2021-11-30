package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LetItSnow {

    public static void main(String[] args) {
        Coord target = readInput();
        firstPart(target);
    }

    private static void firstPart(Coord target) {
        int i = 2;
        Data lastGenerated = new Data(new Coord(1, 1), 20151125L);
        boolean coordFound = false;
        while (!coordFound) {
            ++i;
            for (int y = i - 1; y >= 1 && !coordFound; --y) {
                int x = i - y;
                Data data = new Data(new Coord(x, y), (lastGenerated.value * 252533) % 33554393);
                lastGenerated = data;
                if (x == target.x && y == target.y) {
                    coordFound = true;
                }
            }
        }
        System.out.println(lastGenerated.value());
    }

    private static record Coord(int x, int y) {
        private static final Pattern INPUT_PATTERN = Pattern.compile(
                "^To continue, please consult the code grid in the manual\\.  Enter the code at row (\\d+), column (\\d+)\\.$");

        static Coord mapInput(String line) {
            var m = INPUT_PATTERN.matcher(line);
            if (!m.matches()) {
                throw new IllegalArgumentException("can't match line: " + line);
            }
            return new Coord(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
        }
    }

    private static record Data(Coord coord, long value) {
    }

    private static Coord readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(LetItSnow.class.getResourceAsStream("/day25.txt")))) {
            return Coord.mapInput(reader.lines().collect(Collectors.joining()).trim());
        } catch (Exception e) {
            return Coord.mapInput(INPUT);
        }
    }

    private static final String INPUT = "To continue, please consult the code grid in the manual.  Enter the code at row 3010, column 3019.";
}
