package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class RpgSimulator20xx {
    private static final List<Weapon> WEAPONS = List.of(new Weapon("Dagger", 8, 4, 0),
            new Weapon("Shortsword", 10, 5, 0), new Weapon("Warhammer", 25, 6, 0), new Weapon("Longsword", 40, 7, 0),
            new Weapon("Greataxe", 74, 8, 0));
    private static final List<Armor> ARMORS = List.of(new Armor("Leather", 13, 0, 1), new Armor("Chainmail", 31, 0, 2),
            new Armor("Splintmail", 53, 0, 3), new Armor("Bandedmail", 75, 0, 4), new Armor("Plantemail", 102, 0, 5));
    private static final List<Ring> RINGS = List.of(new Ring("Damage +1", 25, 1, 0), new Ring("Damage +2", 50, 2, 0),
            new Ring("Damage +3", 100, 3, 0), new Ring("Defense +1", 20, 0, 1), new Ring("Defense +2", 40, 0, 2),
            new Ring("Defense +3", 80, 0, 3));

    public static void main(String[] args) {
        var boss = readBoss();
        var setups = allSetups();
        firstPart(boss, setups);
        secondPart(boss, setups);
    }

    private static void firstPart(Fighter boss, Collection<Setup> setups) {
        cheapestWin(setups, boss);
    }

    private static void secondPart(Fighter boss, Collection<Setup> setups) {
        mostExpensiveLoses(setups, boss);
    }

    private static void cheapestWin(Collection<Setup> setups, Fighter boss) {
        Queue<Setup> cheapQueue = new PriorityQueue<>(Comparator.comparingInt(Setup::cost));
        cheapQueue.addAll(setups);
        simulate(cheapQueue, boss, false);
    }

    private static void mostExpensiveLoses(Collection<Setup> setups, Fighter boss) {
        Queue<Setup> expensiveQueue = new PriorityQueue<>(Comparator.comparing(Setup::cost, Comparator.reverseOrder()));
        expensiveQueue.addAll(setups);
        simulate(expensiveQueue, boss, true);
    }

    private static void simulate(Queue<Setup> setups, Fighter boss, boolean bossShouldWin) {
        Setup lastSetup = null;
        boolean expectedPlayerWins = false;
        while (!setups.isEmpty() && !expectedPlayerWins) {
            lastSetup = setups.poll();
            boolean playerWins = isWinningSetup(lastSetup, boss);
            if (bossShouldWin) {
                expectedPlayerWins = !playerWins;
            } else {
                expectedPlayerWins = playerWins;
            }
        }
        System.out.println(lastSetup.cost());
    }

    private static boolean isWinningSetup(Setup setup, Fighter boss) {
        var player = Fighter.createPlayer(setup);
        simulate(player, boss.clone());
        return player.isAlive();
    }

    private static void simulate(Fighter player, Fighter boss) {
        while (player.isAlive() && boss.isAlive()) {
            player.attack(boss);
            if (boss.isAlive()) {
                boss.attack(player);
            }
        }
    }

    private static Collection<Setup> allSetups() {
        Set<Setup> setups = new HashSet<>();
        for (Weapon weapon : WEAPONS) {
            for (Armor armor : ARMORS) {
                setups.add(new Setup(weapon, armor, Set.of()));
                setups.add(new Setup(weapon, null, Set.of()));
                for (Ring ring1 : RINGS) {
                    for (Ring ring2 : RINGS) {
                        if (ring1 != ring2) {
                            setups.add(new Setup(weapon, armor, Set.of(ring1, ring2)));
                            setups.add(new Setup(weapon, null, Set.of(ring1, ring2)));
                        } else {
                            setups.add(new Setup(weapon, armor, Set.of(ring1)));
                            setups.add(new Setup(weapon, null, Set.of(ring1)));
                        }
                    }
                }
            }
        }
        return setups;
    }

    private static sealed interface Item permits Weapon,Armor,Ring,Setup {
        String name();

        int cost();

        int damage();

        int armor();
    }

    private static record Weapon(String name, int cost, int damage, int armor) implements Item {
    }

    private static record Armor(String name, int cost, int damage, int armor) implements Item {
    }

    private static record Ring(String name, int cost, int damage, int armor) implements Item {
    }

    private static record Setup(Weapon weapon, Armor chestPlate, Set<Ring> rings) implements Item {
        @Override
        public int cost() {
            return weapon.cost() + rings.stream().mapToInt(Ring::cost).sum()
                    + (chestPlate != null ? chestPlate.cost() : 0);
        }

        @Override
        public int damage() {
            return weapon.damage() + rings.stream().mapToInt(Ring::damage).sum();
        }

        @Override
        public int armor() {
            return (chestPlate != null ? chestPlate.armor() : 0) + rings.stream().mapToInt(Ring::armor).sum();
        }

        @Override
        public String name() {
            return weapon.name() + "-" + (chestPlate != null ? chestPlate.name() : "") + "-["
                    + rings.stream().map(Ring::name).collect(Collectors.joining(", ")) + "]";
        }
    }

    private static class Fighter implements Cloneable {
        int hitPoints;
        int damage;
        int armor;

        public Fighter(int hitPoints, int damage, int armor) {
            this.hitPoints = hitPoints;
            this.damage = damage;
            this.armor = armor;
        }

        @Override
        public Fighter clone() {
            try {
                return (Fighter) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException(e);
            }
        }

        void recieveDamage(int damage) {
            hitPoints -= Math.max(damage - armor, 1);
        }

        void attack(Fighter fighter) {
            fighter.recieveDamage(damage);
        }

        boolean isDead() {
            return hitPoints <= 0;
        }

        boolean isAlive() {
            return !isDead();
        }

        static Fighter createPlayer(Setup setup) {
            return new Fighter(100, setup.damage(), setup.armor());
        }
    }

    private static Fighter readBoss() {
        List<String> stats = readInput();
        return new Fighter(fromStat(stats.get(0)), fromStat(stats.get(1)), fromStat(stats.get(2)));
    }

    private static int fromStat(String stat) {
        return Integer.parseInt(stat.split(": ")[1]);
    }

    private static List<String> readInput() {
        try (var r = new BufferedReader(
                new InputStreamReader(RpgSimulator20xx.class.getResourceAsStream("/day21.txt")))) {
            return r.lines().toList();
        } catch (Exception e) {
            return INPUT.lines().toList();
        }
    }

    private static final String INPUT = """
            Hit Points: 109
            Damage: 8
            Armor: 2""";
}
