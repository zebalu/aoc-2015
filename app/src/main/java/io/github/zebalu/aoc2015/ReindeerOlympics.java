package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ReindeerOlympics {
    public static void main(String[] args) {
        var reindeers = readInput();
        var max = reindeers.stream().mapToInt(r->r.distanceIn(2503)).max().orElseThrow();
        System.out.println(max);
        var race = new Race(2503, reindeers);
        race.executeRace();
        System.out.println(race.maxPoints());
    }

    private static class Reindeer {
        private static final Pattern REINDEER_PATTERN = Pattern
                .compile("(\\S+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");
        private final String name;
        private final int speed;
        private final int stamina;
        private final int sleep;
        private final int fullLapTime;
        
        private boolean canFly = true;
        private int traveled = 0;
        private int remainingTicks;

        Reindeer(String name, int speed, int stamina, int sleep) {
            this.name = name;
            this.speed = speed;
            this.stamina = stamina;
            this.sleep = sleep;
            fullLapTime = stamina + sleep;
            remainingTicks = stamina;
        }

        int distanceIn(int time) {
            int remainingTime = time;
            int distance = speed * stamina * (remainingTime / fullLapTime);
            remainingTime = time % fullLapTime;
            if (remainingTime > stamina) {
                distance += speed * stamina;
            } else {
                distance += speed * remainingTime;
            }
            return distance;
        }
        void tick() {
            if(canFly) {
                traveled+=speed;
            }
            --remainingTicks;
            if(remainingTicks<=0) {
                remainingTicks=canFly?sleep:stamina;
                canFly=!canFly;
            }
        }
        
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Reindeer r) {
                return name.equals(r.name);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return name.hashCode();
        }

        static Reindeer parse(String description) {
            var m = REINDEER_PATTERN.matcher(description);
            if (!m.matches()) {
                throw new IllegalArgumentException("Can not parse: '" + description + "'");
            }
            return new Reindeer(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)));
        }
    }
    
    private static class Race {
        private final int raceTime;
        private final Map<Reindeer, Integer> scoreBoard = new HashMap<>();
        
        Race(int time, List<Reindeer> deers) {
            this.raceTime=time;
            deers.forEach(d->scoreBoard.put(d,0));
        }
        
        void executeRace() {
            for(int i=0; i<raceTime; ++i) {
                scoreBoard.keySet().forEach(Reindeer::tick);
                int maxTraveled = scoreBoard.keySet().stream().mapToInt(r->r.traveled).max().orElseThrow();
                scoreBoard.keySet().stream().filter(d->d.traveled==maxTraveled).forEach(d -> {
                   int points = scoreBoard.get(d); 
                   scoreBoard.put(d, points+1);
                });
            }
        }
        
        int maxPoints() {
            return scoreBoard.values().stream().mapToInt(Integer::intValue).max().orElseThrow();
        }
    }

    private static List<Reindeer> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(ReindeerOlympics.class.getResourceAsStream("/day14.txt")))) {
            return reader.lines().map(Reindeer::parse).toList();
        } catch (Exception e) {
            return INPUT.lines().map(Reindeer::parse).toList();
        }
    }

    private static final String INPUT = """
            Rudolph can fly 22 km/s for 8 seconds, but then must rest for 165 seconds.
            Cupid can fly 8 km/s for 17 seconds, but then must rest for 114 seconds.
            Prancer can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.
            Donner can fly 25 km/s for 6 seconds, but then must rest for 145 seconds.
            Dasher can fly 11 km/s for 12 seconds, but then must rest for 125 seconds.
            Comet can fly 21 km/s for 6 seconds, but then must rest for 121 seconds.
            Blitzen can fly 18 km/s for 3 seconds, but then must rest for 50 seconds.
            Vixen can fly 20 km/s for 4 seconds, but then must rest for 75 seconds.
            Dancer can fly 7 km/s for 20 seconds, but then must rest for 119 seconds.""";
}
