package org.example;

import de.gesundkrank.jskills.GameInfo;
import de.gesundkrank.jskills.Rating;

import java.util.Comparator;

public class Stats {
    private Rating rating;
    private int wins;
    private int losses;
    private int turnsTakenToWin;
    private int turnsTakenToLose;

    public Tier tier() {
        return tier;
    }

    public void tier(Tier tier) {
        this.tier = tier;
    }

    private Tier tier;

    public Stats(GameInfo gameInfo) {
        this.rating = gameInfo.getDefaultRating();
    }

    public Rating rating() {
        return rating;
    }

    public void rating(Rating rating) {
        this.rating = rating;
    }

    public void recordWin(int turnsTaken) {
        wins++;
        turnsTakenToWin += turnsTaken;
    }

    public void recordLoss(int turnsTaken) {
        losses++;
        turnsTakenToLose += turnsTaken;
    }

    public double averageTurnsTakenToWin() {
        return turnsTakenToWin / (double) wins;
    }

    public double averageTurnsTakenToLose() {
        return turnsTakenToLose / (double) losses;
    }

    public int gamesPlayed() {
        return wins + losses;
    }

    public int wins() {
        return wins;
    }

    public int losses() {
        return losses;
    }

    public double winRate() {
        return (double) wins / (double) (wins + losses);
    }
}