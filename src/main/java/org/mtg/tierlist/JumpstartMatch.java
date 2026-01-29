package org.mtg.tierlist;

import com.google.common.eventbus.Subscribe;
import forge.ai.LobbyPlayerAi;
import forge.game.GameLog;
import forge.game.GameRules;
import forge.game.GameType;
import forge.game.Match;
import forge.game.card.Card;
import forge.game.event.GameEventCardChangeZone;
import forge.game.event.GameEventLandPlayed;
import forge.game.event.GameEventSpellAbilityCast;
import forge.game.event.IGameEventVisitor;
import forge.game.player.RegisteredPlayer;
import forge.game.spellability.Spell;
import forge.game.zone.ZoneType;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

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
                new RegisteredPlayer(p1Deck.forgeDeck()).setPlayer(new LobbyPlayerAi("p1", null)),
                new RegisteredPlayer(p2Deck.forgeDeck()).setPlayer(new LobbyPlayerAi("p2", null))
        );

        var rules = new GameRules(GameType.Constructed);
        rules.setGamesPerMatch(1); // we want best of 1

        var match = new Match(rules, players, p1Deck + " vs " + p2Deck);

        var game = match.createGame();
        var cardCollector = new CardCollector();
        game.subscribeToEvents(cardCollector);
        var startTime = Instant.now();
        match.startGame(game); // returns when the game is finished
        var duration = Duration.between(startTime, Instant.now());

        var outcome = game.getOutcome();
        var winner = outcome.getWinningLobbyPlayer();

        if (winner == null) {
            return null;
        }
        if (winner.getName().equals("p1")) {
            return new JumpstartGameOutcome(
                    p1Deck, cardCollector.getCards("p1"),
                    p2Deck, cardCollector.getCards("p2"),
                    duration, outcome.getLastTurnNumber()
            );
        }
        return new JumpstartGameOutcome(
                p2Deck, cardCollector.getCards("p2"),
                p1Deck, cardCollector.getCards("p1"),
                duration, outcome.getLastTurnNumber()
        );
    }

    private class CardCollector extends IGameEventVisitor.Base<Void> {

        private Map<String, Set<String>> cardsPerPlayer = new HashMap<>();

        Set<String> getCards(String player) {
            return cardsPerPlayer.getOrDefault(player, Collections.emptySet());
        }

        @Override
        @Subscribe
        public Void visit(GameEventCardChangeZone event) {
            var to = event.to();
            var card = event.card();
            if (to.getZoneType() == ZoneType.Battlefield
                    && card.getOwner() == card.getController()
                    && !card.isToken()
                    && !card.getType().isBasicLand()
                    && !isThrivingLand(card)
            ) {
                var owner = card.getOwner();
                // this makes a difference for "enters as a copy of" effects. We want to record the name of the card
                // that was actually cast, NOT the permanent it copies
                var paperCard = card.getPaperCard();
                cardsPerPlayer.computeIfAbsent(owner.getName(), p -> new HashSet<>())
                        .add(paperCard.getName());
            }
            return super.visit(event);
        }

        @Override
        @Subscribe
        public Void visit(GameEventSpellAbilityCast event) {
            var spellOrAbility = event.sa();
            var card = spellOrAbility.getHostCard();
            var owner = card.getOwner();
            if (!card.isToken() && owner == card.getController()) {
                var paperCard = card.getPaperCard();
                cardsPerPlayer.computeIfAbsent(owner.getName(), p -> new HashSet<>())
                        .add(paperCard.getName());
            }
            return super.visit(event);
        }

        private static boolean isThrivingLand(Card card) {
            return card.getType().isLand() && card.getName().startsWith("Thriving");
        }
    }
}
