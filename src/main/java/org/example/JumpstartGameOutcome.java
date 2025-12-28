package org.example;

import de.gesundkrank.jskills.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public record JumpstartGameOutcome(JumpstartDeck winner, JumpstartDeck loser, Duration duration, int turnCount) {

    public void updateRatings(GameInfo gameInfo) {
        var newRatings = TrueSkillCalculator.calculateNewRatings(
                gameInfo,
                List.of(newTeam(winner), newTeam(loser)),
                1, 2
        );
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
        deck.getBoosters()
                .forEach(booster -> team.addPlayer(
                        new Player<>(booster),
                        booster.rating()
                ));
        return team;
    }

    public String toString() {
        return "Winner: " + winner + ", Loser: " + loser + ", Duration: " + duration + ", Turns:" + turnCount;
    }
}
