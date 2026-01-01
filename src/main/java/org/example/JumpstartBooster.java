package org.example;

import de.gesundkrank.jskills.GameInfo;
import forge.card.MagicColor;
import forge.item.PaperCard;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class JumpstartBooster {

    private final String setCode;
    private final String name;
    private final List<PaperCard> cards;
    private final Stats stats;
    private final MagicColor.Color color;

    public JumpstartBooster(String setCode, String name, List<PaperCard> cards, GameInfo gameInfo) {
        this.setCode = setCode;
        this.name = name;
        this.cards = cards;
        this.stats = new Stats(gameInfo);
        this.color = cards.stream()
                .map(JumpstartBooster::getCardColorIfSingle)
                .filter(Objects::nonNull)
                .findFirst() // If we can't find any card with a single color, then the pack is colorless.
                .orElse(MagicColor.Color.COLORLESS);
    }

    public List<PaperCard> cards() {
        return cards;
    }

    public String name() {
        return name;
    }

    public Stats stats() {
        return stats;
    }

    public String toString() {
        return name + cards;
    }

    public MagicColor.Color color() {
        return color;
    }

    public static Comparator<JumpstartBooster> bestRatingFirst() {
        return Comparator.comparing((JumpstartBooster b) -> b.stats().rating().getConservativeRating()).reversed();
    }

    private static MagicColor.Color getCardColorIfSingle(PaperCard card) {
        // Won't work for CLU or MKM, but I'm not interested in these anyway...
        var manaCost = card.getRules().getManaCost();
        var colorProfile = manaCost.getColorProfile();
        if (Integer.bitCount(colorProfile) != 1) {
            return null;
        }
        return MagicColor.Color.fromByte(colorProfile);
    }
}
