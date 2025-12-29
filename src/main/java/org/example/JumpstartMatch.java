package org.example;

import forge.ai.AIOption;
import forge.ai.LobbyPlayerAi;
import forge.game.GameRules;
import forge.game.GameType;
import forge.game.Match;
import forge.game.player.RegisteredPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class JumpstartMatch {

    private final JumpstartDeck p1Deck;
    private final JumpstartDeck p2Deck;

    public JumpstartMatch(JumpstartDeck p1Deck, JumpstartDeck p2Deck) {
        this.p1Deck = p1Deck;
        this.p2Deck = p2Deck;
    }

    public static JumpstartMatch randomMatch(JumpstartSet set) {
        return new JumpstartMatch(set.randomDeck(), set.randomDeck());
    }

    public JumpstartGameOutcome play() {
        var players = List.of(
                new RegisteredPlayer(p1Deck.toForgeDeck()).setPlayer(new LobbyPlayerAi("p1", null)),
                new RegisteredPlayer(p2Deck.toForgeDeck()).setPlayer(new LobbyPlayerAi("p2", null))
        );

        var rules = new GameRules(GameType.Constructed);
        rules.setGamesPerMatch(1); // we want best of 1

        var match = new Match(rules, players, p1Deck + " vs " + p2Deck);

        var game = match.createGame();
        var startTime = Instant.now();
        match.startGame(game); // returns when the game is finished
        var duration = Duration.between(startTime, Instant.now());

        var outcome = game.getOutcome();
        if (outcome.getWinningLobbyPlayer().getName().equals("p1")) {
            return new JumpstartGameOutcome(p1Deck, p2Deck, duration, outcome.getLastTurnNumber());
        }
        return new JumpstartGameOutcome(p2Deck, p1Deck, duration, outcome.getLastTurnNumber());
    }
}
