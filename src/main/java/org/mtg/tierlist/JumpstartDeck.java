package org.mtg.tierlist;

import forge.card.MagicColor;
import forge.deck.Deck;
import forge.deck.DeckSection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JumpstartDeck {
    private final List<JumpstartBooster> boosters;
    private final Deck forgeDeck;
    private final String color;

    public JumpstartDeck(JumpstartBooster a, JumpstartBooster b) {
        this.boosters = Stream.of(a, b).sorted(Comparator.comparing(JumpstartBooster::name)).toList();
        this.forgeDeck = toForgeDeck(this.boosters);
        this.color = JumpstartBooster.colors(this.boosters).stream()
                .map(MagicColor.Color::getShortName)
                .collect(Collectors.joining());
    }

    public List<JumpstartBooster> getBoosters() {
        return boosters;
    }

    public boolean usesDifferentBoosters() {
        return !boosters.get(0).equals(boosters.get(1));
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

    /**
     * Returns a Set because a card with the specified name can exist in both boosters of the deck
     */
    public Set<JumpstartCard> getCards(String cardName) {
        return boosters.stream()
                .flatMap(booster -> booster.getCard(cardName).stream())
                .collect(Collectors.toSet());
    }

    public String toString() {
        return boosters.get(0).name() + " & " + boosters.get(1).name();
    }

    public String color() {
        return this.color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JumpstartDeck that = (JumpstartDeck) o;
        return Objects.equals(boosters, that.boosters);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(boosters);
    }
}
