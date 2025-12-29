package org.example;

import de.gesundkrank.jskills.*;
import org.apache.commons.lang3.NotImplementedException;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public record JumpstartGameOutcome(JumpstartDeck winner, JumpstartDeck loser, Duration duration, int turnCount) {

    public void updateRatings(GameInfo gameInfo) {
        var newRatings = TrueSkillCalculator.calculateNewRatings(
                gameInfo,
                List.of(newTeam(winner), newTeam(loser)),
                1, 2
        );
        winner.getBoosters().stream().distinct().forEach(JumpstartBooster::recordWin);
        loser.getBoosters().stream().distinct().forEach(JumpstartBooster::recordLoss);
        updateRatings(winner, newRatings);
        updateRatings(loser, newRatings);
    }

    private static void updateRatings(JumpstartDeck deck, Map<IPlayer, Rating> newRatings) {
        deck.getBoosters().forEach(booster -> updateRatings(booster, newRatings));
    }

    private static void updateRatings(JumpstartBooster booster, Map<IPlayer, Rating> newRatings) {
        newRatings.entrySet().stream()
                .filter(entry -> getBooster(entry.getKey()) == booster)
                .forEach(entry -> getBooster(entry.getKey()).rating(entry.getValue()));
    }

    private static JumpstartBooster getBooster(IPlayer player) {
        var id = ((Player<?>) player).getId();
        return (JumpstartBooster) id;
    }

    private static Team newTeam(JumpstartDeck deck) {
        var team = new Team();
        deck.getBoosters().stream()
                .distinct()
                .forEach(booster -> team.addPlayer(
                        new Player<>(booster),
                        booster.rating()
                ));
        return team;
    }

    public String toString() {
        return "Winner: " + winner + ", Loser: " + loser + ", Duration: " + duration + ", Turns:" + turnCount;
    }

    public String toCsv() {
        return winner.getBoosters().get(0).name() + ',' +
                winner.getBoosters().get(1).name() + ',' +
                loser.getBoosters().get(0).name() + ',' +
                loser.getBoosters().get(1).name() + ',' +
                duration + ',' +
                turnCount;
    }

    public static JumpstartGameOutcome fromCSV(String line, Map<String, JumpstartBooster> boosters) {
        var parts = line.split(",");
        var winner1 = boosters.get(parts[0]);
        var winner2 = boosters.get(parts[1]);
        var loser1 = boosters.get(parts[2]);
        var loser2 = boosters.get(parts[3]);
        var duration = Duration.parse(parts[4]);
        var turnCount = Integer.parseInt(parts[5]);

        var winnerDeck = new JumpstartDeck(winner1, winner2);
        var loserDeck = new JumpstartDeck(loser1, loser2);

        return new JumpstartGameOutcome(winnerDeck, loserDeck, duration, turnCount);
    }
}
