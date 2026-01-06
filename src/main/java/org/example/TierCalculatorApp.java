package org.example;

import de.gesundkrank.jskills.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class TierCalculatorApp {

    private final JumpstartEnvironment environment;
    private final Tournament tournament;
    private final GameInfo gameInfo = GameInfo.getDefaultGameInfo();

    public TierCalculatorApp(JumpstartEnvironment environment, Tournament tournament) {
        this.environment = environment;
        this.tournament = tournament;
    }

    public static void main(String[] args) throws IOException {
        var environment = new WotcJumpstartEnvironment();

        //var tournament = SetBasedTournament.withRandomBoosters(environment, false);
        var tournament = CubeRandomTournament.withMyCubeV2(environment);
        //var tournament = CubeRandomTournament.withMyOwnedBoosters(environment);

        var ratings = new TierCalculatorApp(environment, tournament);
        ratings.printBoosterStats();
        ratings.printDeckStats();
    }

    private void printBoosterStats() throws IOException {
        for (var record : tournament.getRecords()) {
            // There are so many games for each booster than doing more than 1 round doesn't change the booster ratings
            var statsByBooster = calculateStats(record, JumpstartDeck::getBoosters, Stats::winRate, 1);

            var filePath = getRatingFilePath(record, "-boosters.csv");
            try (var out = Files.newBufferedWriter(filePath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
                out.write("Name,Tier,Color,Games Played,Wins,Losses,Win Rate,Conservative Rating,Mean,StdDev,Turns to Win,Turns to Lose\n");
                statsByBooster.entrySet().stream()
                        .sorted(Comparator.comparing(e -> e.getValue().winRate(), Comparator.reverseOrder()))
                        .forEach(e -> {
                            try {
                                out.write(toCsv(e.getKey().name(), e.getKey().color().getShortName(), e.getValue()));
                                out.write("\n");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
            }
        }
    }

    private void printDeckStats() throws IOException {
        for (var record : tournament.getRecords()) {
            // There are a lot of different deck combinations. More than one round is required to get good results.
            var statsByDeck = calculateStats(record, List::of, stats -> stats.rating().getConservativeRating(), 10);

            var filePath = getRatingFilePath(record, "-decks.csv");
            try (var out = Files.newBufferedWriter(filePath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
                out.write("Name,Tier,Color,Games Played,Wins,Losses,Win Rate,Conservative Rating,Mean,StdDev,Turns to Win,Turns to Lose\n");
                statsByDeck.entrySet().stream()
                        .sorted(Comparator.comparing(e -> e.getValue().rating().getConservativeRating(), Comparator.reverseOrder()))
                        .forEach(e -> {
                            try {
                                out.write(toCsv(e.getKey().toString(), e.getKey().color(), e.getValue()));
                                out.write("\n");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
            }
        }
    }

    private Path getRatingFilePath(JumpstartGameRecord record, String suffix) {
        Path folderName = record.folderPath().getFileName();
        return record.folderPath().getParent().getParent()
                .resolve("ratings")
                .resolve(folderName)
                .resolve(record.getName() + suffix);
    }

    private <K> Map<K, Stats> calculateStats(JumpstartGameRecord record, Function<JumpstartDeck, List<K>> keyFunction, Function<Stats, Double> scoreFunction, int rounds) throws IOException {
        var allBoosters = environment.boosters();
        var outcomes = record.load(allBoosters);

        var stats = calculateWinRecord(outcomes, keyFunction);
        calculateRatings(outcomes, keyFunction, stats, rounds);
        calculateTiers(stats.values(), scoreFunction);

        return stats;
    }

    private <K> Map<K, Stats> calculateWinRecord(List<JumpstartGameOutcome> allOutcomes, Function<JumpstartDeck, List<K>> keyFunction) {
        var stats = new HashMap<K, Stats>();
        for (var outcome : allOutcomes) {
            var winKeys = keyFunction.apply(outcome.winner());
            winKeys.forEach(k -> stats.computeIfAbsent(k, k2 -> new Stats(gameInfo)).recordWin(outcome.turnCount()));
            var loseKeys = keyFunction.apply(outcome.loser());
            loseKeys.forEach(k -> stats.computeIfAbsent(k, k2 -> new Stats(gameInfo)).recordLoss(outcome.turnCount()));
        }
        return stats;
    }

    private <K> void calculateRatings(List<JumpstartGameOutcome> outcomes, Function<JumpstartDeck, List<K>> keyFunction, Map<K, Stats> stats, int rounds) throws IOException {
        for (int i = 0; i < rounds; i++) {
            var shuffled = new ArrayList<>(outcomes);
            Collections.shuffle(shuffled);
            var statsBefore = stats.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().rating()));
            for (var outcome : outcomes) {
                var winTeam = new Team();
                var loseTeam = new Team();

                var winKeys = keyFunction.apply(outcome.winner());
                var loseKeys = keyFunction.apply(outcome.loser());

                winKeys.forEach(k -> winTeam.addPlayer(new Player<>(k), stats.get(k).rating()));
                loseKeys.forEach(k -> loseTeam.addPlayer(new Player<>(k), stats.get(k).rating()));

                var newRatings = TrueSkillCalculator.calculateNewRatings(gameInfo, List.of(winTeam, loseTeam), 1, 2);
                newRatings.forEach((player, newRating) -> stats.get(((Player<K>) player).getId()).rating(newRating));
            }

            // Normalize ratings so that the lowest is 0
            var min = stats.values().stream().mapToDouble(s -> s.rating().getConservativeRating()).min().orElseThrow();
            stats.values().stream().forEach(s -> s.rating(new Rating(s.rating().getMean() - min, s.rating().getStandardDeviation())));

            var totalError = stats.entrySet().stream()
                    .mapToDouble(e -> Math.abs(e.getValue().rating().getConservativeRating() - statsBefore.get(e.getKey()).getConservativeRating()))
                    .sum();
            System.out.println("Round " + i + " error: " + (totalError / stats.size()));
        }
    }

    private void calculateTiers(Collection<Stats> list, Function<Stats, Double> scoreFunction) {
        double high = list.stream().mapToDouble(scoreFunction::apply).max().orElseThrow();
        double low = list.stream().mapToDouble(scoreFunction::apply).min().orElseThrow();
        double step = (high - low) / Tier.values().length;
        list.forEach(stat -> {
            double rating = scoreFunction.apply(stat);
            int tierIndex = Tier.values().length - (int) Math.ceil((rating - low) / step);
            // the lowest stat will get tierIndex=6, it should be normalized back to 5
            stat.tier(Tier.values()[Math.min(tierIndex, Tier.values().length - 1)]);
        });
    }

    public String toCsv(String name, String color, Stats stats) {
        var rating = stats.rating();
        return name + "," +
                stats.tier() + "," +
                color + "," +
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
