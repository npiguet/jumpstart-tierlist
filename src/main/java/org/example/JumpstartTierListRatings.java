package org.example;

import de.gesundkrank.jskills.*;
import forge.card.MagicColor;

import java.io.IOException;
import java.util.*;

public class JumpstartTierListRatings extends JumpstartTierList {

    public static void main(String[] args) throws IOException {
        var ratings = new JumpstartTierListRatings();
        ratings.calculateBoosterRatings();
        ratings.calculateDeckRatings();
    }

    public void calculateBoosterRatings() throws IOException {
        var allBoosters = jumpstartSets().stream().flatMap(set -> set.boosters().stream()).toList();

        var record = new JumpstartGameRecord("owned");
        for (var outcome : record.load(allBoosters)) {
            outcome.updateRatings(gameInfo());
        }

        var allBoostersPlayed = allBoosters.stream().filter(booster -> booster.stats().gamesPlayed() > 0).toList();

        calculateTiers(allBoostersPlayed.stream().map(JumpstartBooster::stats).toList());

        System.out.println("Name,Tier,Color,Games Played,Wins,Losses,Win Rate,Conservative Rating,Mean,StdDev,Turns to Win,Turns to Lose");
        allBoostersPlayed.stream()
                .sorted(JumpstartBooster.bestRatingFirst())
                .forEach(b -> {
                    System.out.println(toCsv(b));
                });
        System.out.println("\n\n");
    }

    private void calculateTiers(List<Stats> list) {
        double high = list.stream().mapToDouble(s -> s.rating().getConservativeRating()).max().orElseThrow();
        double low = list.stream().mapToDouble(s -> s.rating().getConservativeRating()).min().orElseThrow();
        double step = (high - low) / Tier.values().length;
        list.forEach(stat -> {
            double rating = stat.rating().getConservativeRating();
            int tierIndex = Tier.values().length - (int)Math.ceil((rating - low) / step);
            // the lowest stat will get tierIndex=6, it should be normalized back to 5
            stat.tier(Tier.values()[Math.min(tierIndex, 5)]);
        });
    }

    public void calculateDeckRatings() throws IOException {
        Map<String, Stats> statsPerDeck = new HashMap<>();

        var allBoosters = jumpstartSets().stream().flatMap(set -> set.boosters().stream()).toList();
        var record = new JumpstartGameRecord("owned");
        var allOutcomes = record.load(allBoosters);

        updateWinRecord(allOutcomes, statsPerDeck);
        calculateRatings(allOutcomes, statsPerDeck);

        System.out.println("Name,Color,Games Played,Wins,Losses,Win Rate,Conservative Rating,Mean,StdDev,Turns to Win,Turns to Lose");
        statsPerDeck.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().rating().getConservativeRating(), Comparator.reverseOrder()))
                .forEach(e -> {
                    System.out.println(toCsv(e.getKey(), MagicColor.Color.COLORLESS, e.getValue()));
                });
    }

    private void updateWinRecord(List<JumpstartGameOutcome> allOutcomes, Map<String, Stats> statsPerDeck) {
        for (var outcome : allOutcomes) {
            String winKey = outcome.winner().toString();
            Stats winStats = statsPerDeck.computeIfAbsent(winKey, k -> new Stats(gameInfo()));
            String loseKey = outcome.loser().toString();
            Stats loseStats = statsPerDeck.computeIfAbsent(loseKey, k -> new Stats(gameInfo()));

            winStats.recordWin(outcome.turnCount());
            loseStats.recordLoss(outcome.turnCount());
        }
    }

    private void calculateRatings(List<JumpstartGameOutcome> outcomes, Map<String, Stats> statsPerDeck) throws IOException {
//        var shuffled = new ArrayList<>(outcomes);
//        Collections.shuffle(shuffled);
        for (var outcome : outcomes) {
            String winKey = outcome.winner().toString();
            Stats winStats = statsPerDeck.computeIfAbsent(winKey, k -> new Stats(gameInfo()));
            String loseKey = outcome.loser().toString();
            Stats loseStats = statsPerDeck.computeIfAbsent(loseKey, k -> new Stats(gameInfo()));

            var newRatings = TrueSkillCalculator.calculateNewRatings(
                    gameInfo(),
                    List.of(
                            new Team(new Player<>(winKey), winStats.rating()),
                            new Team(new Player<>(loseKey), loseStats.rating())
                    ),
                    1, 2
            );
            winStats.rating(getRating(winKey, newRatings));
            loseStats.rating(getRating(loseKey, newRatings));
        }
    }

    private static Rating getRating(String key, Map<IPlayer, Rating> ratings) {
        return ratings.entrySet().stream()
                .filter(e -> ((Player<?>) e.getKey()).getId().equals(key))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow();
    }

    public String toCsv(JumpstartBooster booster) {
        return toCsv(booster.name(), booster.color(), booster.stats());
    }

    public String toCsv(String name, MagicColor.Color color, Stats stats) {
        var rating = stats.rating();
        return name + "," +
                stats.tier() + "," +
                color.getShortName() + "," +
                stats.gamesPlayed() + "," +
                stats.wins() + "," +
                stats.losses() + "," +
                stats.winRate() + "," +
                rating.getConservativeRating() + "," +
                rating.getMean() + "," +
                rating.getStandardDeviation() + "," +
                stats.averageTurnsTakenToWin() + "," +
                stats.averageTurnsTakenToLose();
    }
}
