package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ElvesLookElvesSay {

    public static void main(String[] args) {
        String line = readInput();
        firstPart(line);
        secondPart(line);
    }

    private static void firstPart(String line) {
        conwayRepeat(line, 40);
    }

    private static void secondPart(String line) {
        conwayRepeat(line, 50);
    }

    private static void conwayRepeat(String startLine, int repeats) {
        String line = startLine;
        for (int r = 0; r < repeats; ++r) {
            line = calcualteNewLine(line);
        }
        System.out.println(line.length());
    }

    private static String calcualteNewLine(String line) {
        StringBuilder newLine = new StringBuilder();
        int i = 0;
        while (i < line.length()) {
            i = lookAndSay(line, newLine, i);
        }
        return newLine.toString();
    }

    private static int lookAndSay(String line, StringBuilder newLine, int from) {
        int i = from;
        while (i < line.length() && line.charAt(i) == line.charAt(from)) {
            ++i;
        }
        newLine.append(i - from);
        newLine.append(line.charAt(from));
        return i;
    }

    private static String readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(ElvesLookElvesSay.class.getResourceAsStream("/day10.txt")))) {
            return reader.lines().collect(Collectors.joining());
        } catch (Exception e) {
            return INPUT;
        }
    }

    private static final String INPUT = "3113322113";
}
