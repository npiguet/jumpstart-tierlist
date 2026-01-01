package org.example;

import forge.card.CardRules;
import forge.card.CardType;
import forge.card.MagicColor;
import forge.card.mana.ManaAtom;
import forge.card.mana.ManaCost;
import forge.card.mana.ManaCostShard;
import forge.deck.CardPool;
import forge.deck.Deck;
import forge.deck.DeckSection;
import forge.item.PaperCard;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JumpstartDeck {
    private final List<JumpstartBooster> boosters;
    private final Deck forgeDeck;

    public JumpstartDeck(JumpstartBooster a, JumpstartBooster b) {
        this.boosters = Stream.of(a, b).sorted(Comparator.comparing(JumpstartBooster::name)).toList();
        this.forgeDeck = toForgeDeck(this.boosters);
    }

    public List<JumpstartBooster> getBoosters() {
        return boosters;
    }

    public Deck forgeDeck() {
        return forgeDeck;
    }

    private static Deck toForgeDeck(List<JumpstartBooster> boosters) {
        Deck deck = new Deck();
        var main = deck.getOrCreate(DeckSection.Main);
        boosters.forEach(b -> main.add(b.cards()));
        return deck;
    }

    public String toString() {
        return boosters.get(0).name() + " & " + boosters.get(1).name();
    }
}
