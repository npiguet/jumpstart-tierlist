package org.example;

import forge.deck.Deck;
import forge.deck.DeckSection;

import java.util.List;

public class JumpstartDeck {
    private final List<JumpstartBooster> boosters;

    public JumpstartDeck(JumpstartBooster a, JumpstartBooster b) {
        this.boosters = List.of(a, b);
    }

    public List<JumpstartBooster> getBoosters() {
        return boosters;
    }

    public Deck toForgeDeck() {
        Deck deck = new Deck();
        var main = deck.getOrCreate(DeckSection.Main);
        boosters.forEach(b -> main.add(b.cards()));
        return deck;
    }

    public String toString() {
        return boosters.get(0).name() + " & " + boosters.get(1).name();
    }
}
