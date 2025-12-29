package org.example;

import java.io.IOException;

public class JumpstartTierListRatings extends JumpstartTierList {

    public static void main(String[] args) throws IOException {
        var ratings = new JumpstartTierListRatings();
        ratings.calculateRatings();
    }

    public void calculateRatings() throws IOException {
        var allBoosters = jumpstartSets().stream().flatMap(set -> set.boosters().stream()).toList();

        for (var set : jumpstartSets()) {
            var record = new JumpstartGameRecord(set.code());
            for (var outcome : record.load(allBoosters)) {
                outcome.updateRatings(gameInfo());
            }

            System.out.println("Name,Games Played,Win Rate,Conservative Rating,Mean,StdDev,Turns to Win,Turns to Lose");
            set.boosters().stream()
                    .sorted(JumpstartBooster.bestRatingFirst())
                    .forEach(b -> {
                        System.out.println(toCsv(b));
                    });
            System.out.println("\n\n");
        }
    }

    public String toCsv(JumpstartBooster booster) {
        var rating = booster.rating();
        return booster.name() + "," +
                booster.gamesPlayed() + "," +
                booster.winRate() + "," +
                rating.getConservativeRating() + "," +
                rating.getMean() + "," +
                rating.getStandardDeviation() + "," +
                booster.averageTurnsTakenToWin() + "," +
                booster.averageTurnsTakenToLose();
    }
}
