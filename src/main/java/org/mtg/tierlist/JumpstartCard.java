package org.mtg.tierlist;

import forge.item.PaperCard;

public record JumpstartCard(PaperCard card, JumpstartBooster booster, int nCopies) {

    // The chance to draw at least one copy of a card in a 40 cards deck within the first 16 cards
    private static final double[] DRAW_CHANCE = {0, 0.4, 0.646, 0.795};

    public int adjustedGameCount(int originalGameCount) {
        if (nCopies <= 1) {
            return originalGameCount;
        }
        return (int) Math.round(originalGameCount * DRAW_CHANCE[1] / DRAW_CHANCE[nCopies]);
    }

    public String toString() {
        return nCopies + "x " + card.getName();
    }
}
