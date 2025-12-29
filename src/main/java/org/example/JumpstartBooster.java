package org.example;

import de.gesundkrank.jskills.GameInfo;
import de.gesundkrank.jskills.Rating;
import forge.item.PaperCard;

import java.util.Comparator;
import java.util.List;

public class JumpstartBooster {

    private final String setCode;
    private final String name;
    private final List<PaperCard> cards;
    private Rating rating;
    private int wins;
    private int losses;

    public JumpstartBooster(String setCode, String name, List<PaperCard> cards, GameInfo gameInfo) {
        this.setCode = setCode;
        this.name = name;
        this.cards = cards;
        this.rating = gameInfo.getDefaultRating();
    }

    public List<PaperCard> cards() {
        return cards;
    }

    public String name() {
        return name;
    }

    public Rating rating() {
        return rating;
    }

    public void rating(Rating rating) {
        this.rating = rating;
    }

    public void recordWin() {
        wins++;
    }

    public void recordLoss() {
        losses++;
    }

    public int gamesPlayed() {
        return wins + losses;
    }

    public double winRate() {
        return (double) wins / (double) (wins + losses);
    }

    public String toString() {
        return name + cards;
    }

    public static Comparator<JumpstartBooster> bestRatingFirst() {
        return Comparator.comparing((JumpstartBooster b) -> b.rating.getConservativeRating()).reversed();
    }
}
