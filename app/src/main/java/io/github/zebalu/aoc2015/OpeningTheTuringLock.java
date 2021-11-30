package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class OpeningTheTuringLock {

    public static void main(String[] args) {
        var instr = readInput();
        firstPart(instr);
        secondPart(instr);
    }

    private static void firstPart(List<Instruction> instr) {
        Computer computer = new Computer();
        computer.execute(instr);
        System.out.println(computer.b);
    }

    private static void secondPart(List<Instruction> instr) {
        Computer computer = new Computer();
        computer.a = 1;
        computer.execute(instr);
        System.out.println(computer.b);
    }

    private static class Computer {
        int ip = 0;
        int a = 0;
        int b = 0;

        void execute(List<Instruction> instructions) {
            while (0 <= ip && ip < instructions.size()) {
                instructions.get(ip).apply(this);
            }
        }
    }

    private enum Key {
        A(computer -> computer.a, (computer, integer) -> computer.a = integer),
        B(computer -> computer.b, (computer, integer) -> computer.b = integer);

        private final Function<Computer, Integer> get;
        private final BiConsumer<Computer, Integer> set;

        Key(Function<Computer, Integer> get, BiConsumer<Computer, Integer> set) {
            this.set = set;
            this.get = get;
        }

        static Key parse(String id) {
            switch (id) {
            case "a":
                return A;
            case "b":
                return B;
            default:
                throw new IllegalArgumentException("unkown key: " + id);
            }
        }
    }

    private static sealed interface Instruction permits Half,Triple,Inc,Jump,JumpEven,JumpOne {
        void apply(Computer computer);
    }

    private static final class Half implements Instruction {
        private final Key key;

        Half(Key key) {
            this.key = key;
        }

        @Override
        public void apply(Computer computer) {
            int original = key.get.apply(computer);
            key.set.accept(computer, original / 2);
            computer.ip += 1;
        }
    }

    private static final class Triple implements Instruction {
        private final Key key;

        Triple(Key key) {
            this.key = key;
        }

        @Override
        public void apply(Computer computer) {
            int original = key.get.apply(computer);
            key.set.accept(computer, original * 3);
            computer.ip += 1;
        }
    }

    private static final class Inc implements Instruction {
        private final Key key;

        Inc(Key key) {
            this.key = key;
        }

        @Override
        public void apply(Computer computer) {
            int original = key.get.apply(computer);
            key.set.accept(computer, original + 1);
            computer.ip += 1;
        }
    }

    private static final class Jump implements Instruction {
        private final int diff;

        Jump(int diff) {
            this.diff = diff;
        }

        @Override
        public void apply(Computer computer) {
            computer.ip += diff;
        }
    }

    private static final class JumpEven implements Instruction {
        private final Key key;
        private final int diff;

        JumpEven(Key key, int diff) {
            this.key = key;
            this.diff = diff;
        }

        @Override
        public void apply(Computer computer) {
            int original = key.get.apply(computer);
            if (original % 2 == 0) {
                computer.ip += diff;
            } else {
                computer.ip += 1;
            }
        }
    }

    private static final class JumpOne implements Instruction {
        private final Key key;
        private final int diff;

        JumpOne(Key key, int diff) {
            this.key = key;
            this.diff = diff;
        }

        @Override
        public void apply(Computer computer) {
            int original = key.get.apply(computer);
            if (original == 1) {
                computer.ip += diff;
            } else {
                computer.ip += 1;
            }
        }
    }

    private static Instruction parse(String line) {
        if (line.startsWith("hlf")) {
            return new Half(Key.parse(line.split(" ")[1]));
        } else if (line.startsWith("tpl")) {
            return new Triple(Key.parse(line.split(" ")[1]));
        } else if (line.startsWith("inc")) {
            return new Inc(Key.parse(line.split(" ")[1]));
        } else if (line.startsWith("jmp")) {
            return new Jump(Integer.parseInt(line.split(" ")[1]));
        } else if (line.startsWith("jie")) {
            var cutComa = line.split(", ");
            var cutReg = cutComa[0].split(" ");
            return new JumpEven(Key.parse(cutReg[1]), Integer.parseInt(cutComa[1]));
        } else if (line.startsWith("jio")) {
            var cutComa = line.split(", ");
            var cutReg = cutComa[0].split(" ");
            return new JumpOne(Key.parse(cutReg[1]), Integer.parseInt(cutComa[1]));
        }
        throw new IllegalArgumentException("unknown line: " + line);
    }

    private static List<Instruction> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(OpeningTheTuringLock.class.getResourceAsStream("/day23.txt")))) {
            return reader.lines().map(OpeningTheTuringLock::parse).toList();
        } catch (Exception e) {
            return INPUT.lines().map(OpeningTheTuringLock::parse).toList();
        }
    }

    private static final String INPUT = """
            jio a, +18
            inc a
            tpl a
            inc a
            tpl a
            tpl a
            tpl a
            inc a
            tpl a
            inc a
            tpl a
            inc a
            inc a
            tpl a
            tpl a
            tpl a
            inc a
            jmp +22
            tpl a
            inc a
            tpl a
            inc a
            inc a
            tpl a
            inc a
            tpl a
            inc a
            inc a
            tpl a
            tpl a
            inc a
            inc a
            tpl a
            inc a
            inc a
            tpl a
            inc a
            inc a
            tpl a
            jio a, +8
            inc b
            jie a, +4
            tpl a
            inc a
            jmp +2
            hlf a
            jmp -7""";
}
