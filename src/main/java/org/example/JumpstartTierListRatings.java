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

            set.boosters().stream()
                    .sorted(JumpstartBooster.bestRatingFirst())
                    .forEach(b -> {
                        System.out.println(b.name() + ": " + b.rating().getConservativeRating() + " -> " + b.rating());
                    });
            System.out.println("\n\n");
        }
    }
}
