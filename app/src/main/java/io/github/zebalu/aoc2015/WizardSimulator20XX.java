package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class WizardSimulator20XX {

    public static void main(String[] args) {
        var bossStats = readInput();
        firstPart(bossStats);
        secondPart(bossStats);
    }

    private static void firstPart(List<Integer> bossStats) {
        System.out.println(findCheapestWinEasy(bossStats.get(0), bossStats.get(1)));
    }

    private static void secondPart(List<Integer> bossStats) {
        System.out.println(findCheapestWinHard(bossStats.get(0), bossStats.get(1)));
    }

    private static State easyStartState(int bossHitPoints, int bossDamage) {
        return new State(50, 500, 0, bossHitPoints, bossDamage, 0, 0, 0, false);
    }

    private static State hardStartState(int bossHitPoints, int bossDamage) {
        return new State(50, 500, 0, bossHitPoints, bossDamage, 0, 0, 0, true);
    }

    private static int findCheapestWinEasy(int bossHitPoints, int bossDamage) {
        return findCheapestWin(easyStartState(bossHitPoints, bossDamage));
    }

    private static int findCheapestWinHard(int bossHitPoints, int bossDamage) {
        return findCheapestWin(hardStartState(bossHitPoints, bossDamage));
    }

    private static int findCheapestWin(State startState) {
        Queue<Step> queue = new PriorityQueue<>();
        for (Action action : Action.values()) {
            queue.add(createStep(startState, action, 0));
        }
        boolean winingFound = false;
        Step lastStep = null;
        while (!queue.isEmpty() && !winingFound) {
            lastStep = queue.poll();
            var nexts = lastStep.state.round(lastStep.toDo());
            for (Action action : nexts) {
                queue.add(createStep(lastStep.state(), action, lastStep.spentMana()));
            }
            winingFound = lastStep.state().playerIsAlive() && !lastStep.state().bossIsAlive();
        }
        return lastStep.spentMana();
    }

    private static Step createStep(State startState, Action action, int costSoFar) {
        return new Step(costSoFar + action.manaRequirement, action, startState.clone());
    }

    private static class State implements Cloneable {
        int playerHp;
        int playerMana;
        int playerArmour;
        int bossHp;
        int bossDamage;
        int shieldTick;
        int poisonTick;
        int rechargeTick;
        List<Action> history = new ArrayList<>();
        boolean hard;

        public State(int playerHp, int playerMana, int playerArmour, int bossHp, int bossDamage, int shieldTick,
                int poisonTick, int rechargeTick, boolean hard) {
            super();
            this.playerHp = playerHp;
            this.playerMana = playerMana;
            this.playerArmour = playerArmour;
            this.bossHp = bossHp;
            this.bossDamage = bossDamage;
            this.shieldTick = shieldTick;
            this.poisonTick = poisonTick;
            this.rechargeTick = rechargeTick;
            this.hard = hard;
        }

        @Override
        public State clone() {
            try {
                State clone = (State) super.clone();
                clone.history = new ArrayList<>(history);
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException(e);
            }
        }

        List<Action> round(Action action) {
            history.add(action);
            if (hard) {
                playerHp -= 1;
                if (!playerIsAlive()) {
                    return List.of();
                }
            }
            applyEffects();
            applyAction(action);
            applyEffects();
            if (bossIsAlive()) {
                damagePlayer(bossDamage);
            }
            if (!isFinished()) {
                return possibleFollowingActions();
            }
            return List.of();
        }

        private void applyEffects() {
            --shieldTick;
            if (shieldTick == 0) {
                playerArmour -= 7;
            }
            --poisonTick;
            if (poisonTick >= 0) {
                bossHp -= 3;
            }
            --rechargeTick;
            if (rechargeTick >= 0) {
                playerMana += 101;
            }
        }

        private void applyAction(Action action) {
            playerMana -= action.manaRequirement;
            switch (action) {
            case MAGIC_MISSLE -> bossHp -= 4;
            case DRAIN -> {
                bossHp -= 2;
                playerHp += 2;
            }
            case SHIELD -> {
                playerArmour += 7;
                shieldTick = 6;
            }
            case POISON -> poisonTick = 6;
            case RECHARGE -> rechargeTick = 5;
            }
        }

        boolean bossIsAlive() {
            return bossHp > 0;
        }

        boolean playerIsAlive() {
            return playerHp > 0 && playerMana > 0;
        }

        boolean isFinished() {
            return !(bossIsAlive() && playerIsAlive());
        }

        void damagePlayer(int damage) {
            playerHp -= Math.max(1, damage - playerArmour);
        }

        List<Action> possibleFollowingActions() {
            List<Action> result = new ArrayList<>();
            if (playerMana >= Action.MAGIC_MISSLE.manaRequirement) {
                result.add(Action.MAGIC_MISSLE);
            }
            if (playerMana >= Action.DRAIN.manaRequirement) {
                result.add(Action.DRAIN);
            }
            if (playerMana >= Action.SHIELD.manaRequirement && shieldTick <= 1) {
                result.add(Action.SHIELD);
            }
            if (playerMana >= Action.POISON.manaRequirement && poisonTick <= 1) {
                result.add(Action.POISON);
            }
            if (playerMana >= Action.RECHARGE.manaRequirement && rechargeTick <= 1) {
                result.add(Action.RECHARGE);
            }
            return result;
        }

        @Override
        public String toString() {
            return "State [playerHp=" + playerHp + ", playerMana=" + playerMana + ", playerArmour=" + playerArmour
                    + ", bossHp=" + bossHp + ", bossDamage=" + bossDamage + ", shieldTick=" + shieldTick
                    + ", poisonTick=" + poisonTick + ", rechargeTick=" + rechargeTick + "]";
        }

    }

    private static enum Action {
        MAGIC_MISSLE(53), DRAIN(73), SHIELD(113), POISON(173), RECHARGE(229);

        final int manaRequirement;

        Action(int manaRequiremetn) {
            this.manaRequirement = manaRequiremetn;
        }
    }

    private static record Step(int spentMana, Action toDo, State state) implements Comparable<Step> {

        private static final Comparator<Step> STEP_COMPARATOR = Comparator.comparingInt(Step::spentMana);

        @Override
        public int compareTo(Step o) {
            return STEP_COMPARATOR.compare(this, o);
        }

    }

    private static List<Integer> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(WizardSimulator20XX.class.getResourceAsStream("/day22.txt")))) {
            return reader.lines().map(s -> Integer.parseInt(s.split(": ")[1])).toList();
        } catch (Exception e) {
            return INPUT.lines().map(s -> Integer.parseInt(s.split(": ")[1])).toList();
        }
    }

    private static final String INPUT = """
            Hit Points: 71
            Damage: 10""";

}
