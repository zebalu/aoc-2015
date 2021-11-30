package io.github.zebalu.aoc2015.main;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.github.zebalu.aoc2015.AllInASingleNight;
import io.github.zebalu.aoc2015.AuntSue;
import io.github.zebalu.aoc2015.CorporatePolicy;
import io.github.zebalu.aoc2015.DoesntHeHaveInternElvesForThis;
import io.github.zebalu.aoc2015.ElvesLookElvesSay;
import io.github.zebalu.aoc2015.IWasToldThereWouldBeNoMath;
import io.github.zebalu.aoc2015.InfiniteElvesAndInfiniteHouses;
import io.github.zebalu.aoc2015.ItHangsIntTheBalance;
import io.github.zebalu.aoc2015.JSAbacusFrameworkDotIo;
import io.github.zebalu.aoc2015.KnightsOfTheDinnerTable;
import io.github.zebalu.aoc2015.LetItSnow;
import io.github.zebalu.aoc2015.LikeAGifForYourYard;
import io.github.zebalu.aoc2015.Matchsticks;
import io.github.zebalu.aoc2015.MedicineForRudolph;
import io.github.zebalu.aoc2015.NotQuiteLisp;
import io.github.zebalu.aoc2015.OpeningTheTuringLock;
import io.github.zebalu.aoc2015.PerfectlySphericalHousesInAVacuum;
import io.github.zebalu.aoc2015.ProbablyAFireHazard;
import io.github.zebalu.aoc2015.ReindeerOlympics;
import io.github.zebalu.aoc2015.RpgSimulator20xx;
import io.github.zebalu.aoc2015.ScienceForHungryPeople;
import io.github.zebalu.aoc2015.SomeAssemblyRequired;
import io.github.zebalu.aoc2015.TheIdealStockingStuffer;
import io.github.zebalu.aoc2015.ThereIsNoSuchThingAsTooMuch;
import io.github.zebalu.aoc2015.WizardSimulator20XX;

public class AdventOfCode2015 {

    public static void main(String[] args) {
        List<Day> days = new ArrayList<>(25);
        days.add(new Day(1, NotQuiteLisp.class.getSimpleName(), NotQuiteLisp::main));
        days.add(new Day(2, IWasToldThereWouldBeNoMath.class.getSimpleName(), IWasToldThereWouldBeNoMath::main));
        days.add(new Day(3, PerfectlySphericalHousesInAVacuum.class.getSimpleName(), PerfectlySphericalHousesInAVacuum::main));
        days.add(new Day(4, TheIdealStockingStuffer.class.getSimpleName(), TheIdealStockingStuffer::main));
        days.add(new Day(5, DoesntHeHaveInternElvesForThis.class.getSimpleName(), DoesntHeHaveInternElvesForThis::main));
        days.add(new Day(6, ProbablyAFireHazard.class.getSimpleName(), ProbablyAFireHazard::main));
        days.add(new Day(7, SomeAssemblyRequired.class.getSimpleName(), SomeAssemblyRequired::main));
        days.add(new Day(8, Matchsticks.class.getSimpleName(), Matchsticks::main));
        days.add(new Day(9, AllInASingleNight.class.getSimpleName(), AllInASingleNight::main));
        days.add(new Day(10, ElvesLookElvesSay.class.getSimpleName(), ElvesLookElvesSay::main));
        days.add(new Day(11, CorporatePolicy.class.getSimpleName(), CorporatePolicy::main));
        days.add(new Day(12, JSAbacusFrameworkDotIo.class.getSimpleName(), JSAbacusFrameworkDotIo::main));
        days.add(new Day(13, KnightsOfTheDinnerTable.class.getSimpleName(), KnightsOfTheDinnerTable::main));
        days.add(new Day(14, ReindeerOlympics.class.getSimpleName(), ReindeerOlympics::main));
        days.add(new Day(15, ScienceForHungryPeople.class.getSimpleName(), ScienceForHungryPeople::main));
        days.add(new Day(16, AuntSue.class.getSimpleName(), AuntSue::main));
        days.add(new Day(17, ThereIsNoSuchThingAsTooMuch.class.getSimpleName(), ThereIsNoSuchThingAsTooMuch::main));
        days.add(new Day(18, LikeAGifForYourYard.class.getSimpleName(), LikeAGifForYourYard::main));
        days.add(new Day(19, MedicineForRudolph.class.getSimpleName(), MedicineForRudolph::main));
        days.add(new Day(20, InfiniteElvesAndInfiniteHouses.class.getSimpleName(), InfiniteElvesAndInfiniteHouses::main));
        days.add(new Day(21, RpgSimulator20xx.class.getSimpleName(), RpgSimulator20xx::main));
        days.add(new Day(22, WizardSimulator20XX.class.getSimpleName(), WizardSimulator20XX::main));
        days.add(new Day(23, OpeningTheTuringLock.class.getSimpleName(), OpeningTheTuringLock::main));
        days.add(new Day(24, ItHangsIntTheBalance.class.getSimpleName(), ItHangsIntTheBalance::main));
        days.add(new Day(25, LetItSnow.class.getSimpleName(), LetItSnow::main));
        Instant start = Instant.now();
        days.forEach(Day::execute);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("whole advent took: "+duration.toSeconds()+" seconds");
        
    }

    private static class Day {
        private final int num;
        private final String name;
        private final Consumer<String[]> main;

        Day(int num, String name, Consumer<String[]> main) {
            this.num = num;
            this.name = name;
            this.main = main;
        }

        void execute() {
            System.out.println("================================================================================");
            System.out.println(String.format("%02d Day, %s:", num, name));
            Instant start = Instant.now();
            main.accept(null);
            System.out.println("duration: " + Duration.between(start, Instant.now()).toMillis() + " ms");
            System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        }
    }
}
