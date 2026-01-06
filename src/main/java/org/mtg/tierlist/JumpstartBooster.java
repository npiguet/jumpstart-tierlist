package org.mtg.tierlist;

import forge.card.MagicColor;
import forge.item.PaperCard;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class JumpstartBooster {

    private final String setCode;
    private final String name;
    private final List<PaperCard> cards;
    private final MagicColor.Color color;

    public JumpstartBooster(String setCode, String name, List<PaperCard> cards) {
        this.setCode = setCode;
        this.name = name;
        this.cards = cards;
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

    public String toString() {
        return name + cards;
    }

    public MagicColor.Color color() {
        return color;
    }

    public static List<MagicColor.Color> colors(List<JumpstartBooster> boosters) {
        return boosters.stream()
                .map(JumpstartBooster::color)
                .distinct()
                .sorted(Comparator.comparing(MagicColor.Color::ordinal))
                .toList();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JumpstartBooster that = (JumpstartBooster) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
