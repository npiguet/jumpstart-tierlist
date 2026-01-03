package org.example;

import forge.card.MagicColor;
import forge.deck.Deck;
import forge.deck.DeckSection;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
