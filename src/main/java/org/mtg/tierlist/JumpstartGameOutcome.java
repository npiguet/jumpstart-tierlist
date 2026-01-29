package org.mtg.tierlist;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public record JumpstartGameOutcome(
        JumpstartDeck winner,
        Set<String> winnerCards,
        JumpstartDeck loser,
        Set<String> loserCards,
        Duration duration,
        int turnCount) {

    public String toString() {
        return "Winner: " + winner + ", Loser: " + loser + ", Duration: " + duration + ", Turns:" + turnCount;
    }

    public String toCsv() {
        return winner.getBoosters().get(0).name() + ',' +
                winner.getBoosters().get(1).name() + ',' +
                encodeCardSet(winnerCards) + ',' +
                loser.getBoosters().get(0).name() + ',' +
                loser.getBoosters().get(1).name() + ',' +
                encodeCardSet(loserCards) + ',' +
                duration + ',' +
                turnCount;
    }

    private static String encodeCardSet(Set<String> cards) {
        return cards.stream().map(card -> card.replace(",", "|"))
                .collect(Collectors.joining(";"));
    }

    private static Set<String> decodeCardSet(String encoded) {
        if (encoded.isBlank()) {
            return Set.of();
        }

        return Arrays.stream(encoded.split(";"))
                .map(card -> card.replace("|", ","))
                .collect(toSet());
    }

    public boolean hasNoDuplicateBoosters() {
        var boosters = new HashSet<JumpstartBooster>();
        boosters.addAll(winner.getBoosters());
        boosters.addAll(loser.getBoosters());
        return boosters.size() == 4;
    }

    public boolean hasDifferentDecks() {
        return !winner.equals(loser);
    }

    public List<String> getQualifiedCardsPlayed(JumpstartDeck deck) {
        if (this.winner.equals(deck)) {
            return winnerCards.stream()
                    .map(card -> addPackName(card, this.winner))
                    .toList();
        }
        return loserCards.stream()
                .map(card -> addPackName(card, this.loser))
                .toList();
    }

    private static String addPackName(String cardName, JumpstartDeck deck) {
        return deck.getBoosters().stream()
                .filter(booster -> booster.containsCard(cardName))
                .findFirst()
                .map(booster -> booster.name() + ",\"" + cardName + "\"")
                .orElseThrow(() -> new NoSuchElementException(
                        "Couldn't find booster for [" + cardName + "] in deck [" + deck + "] with boosters [" + deck.getBoosters() + "]"
                ));
    }

    public static JumpstartGameOutcome fromCSV(String line, Map<String, JumpstartBooster> boosters) {
        var parts = line.split(",");
        if (parts.length == 6) {
            // version that doesn't have cards
            var winner1 = boosters.get(parts[0]);
            var winner2 = boosters.get(parts[1]);
            var loser1 = boosters.get(parts[2]);
            var loser2 = boosters.get(parts[3]);
            var duration = Duration.parse(parts[4]);
            var turnCount = Integer.parseInt(parts[5]);

            var winnerDeck = new JumpstartDeck(winner1, winner2);
            var loserDeck = new JumpstartDeck(loser1, loser2);

            return new JumpstartGameOutcome(winnerDeck, Set.of(), loserDeck, Set.of(), duration, turnCount);
        }

        if (parts.length == 8) {
            // version with the cards
            var winner1 = boosters.get(parts[0]);
            var winner2 = boosters.get(parts[1]);
            var winnerCards = decodeCardSet(parts[2]);
            var loser1 = boosters.get(parts[3]);
            var loser2 = boosters.get(parts[4]);
            var loserCards = decodeCardSet(parts[5]);
            var duration = Duration.parse(parts[6]);
            var turnCount = Integer.parseInt(parts[7]);

            var winnerDeck = new JumpstartDeck(winner1, winner2);
            var loserDeck = new JumpstartDeck(loser1, loser2);

            return new JumpstartGameOutcome(winnerDeck, winnerCards, loserDeck, loserCards, duration, turnCount);
        }

        throw new UnsupportedOperationException("Unsupported CSV version");
    }
}
