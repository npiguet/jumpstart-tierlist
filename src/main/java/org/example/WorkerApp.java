package org.example;

import java.io.IOException;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Hello world!
 *
 */
public class WorkerApp extends JumpstartTierList {
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        // the game AI is pretty unstable, and often times out or otherwise requires the program to be killed.
        // So we'll separate playing games from calculating rating results. And we'll accumulate results for games
        // over multiple runs

        var player = new WorkerApp();
        player.playGames(100_000, player::randomOwnedMatch);
    }

    public void playGames(int numberOfGames, Supplier<MatchAndRecord> matchSupplier) throws IOException {
        for (int i = 0; i < numberOfGames; i++) {
            var matchAndRecord = matchSupplier.get();
            var outcome = matchAndRecord.match.play();
            if (outcome != null) {
                matchAndRecord.record.append(outcome);
                System.out.println("Game " + (i + 1) + ": " + outcome);
            }
        }
    }

    private MatchAndRecord randomCubeMatch() {
        return new MatchAndRecord(
                JumpstartMatch.randomMatch(cube()),
                new JumpstartGameRecord("cube")
        );
    }

    private MatchAndRecord randomOwnedMatch() {
        return new MatchAndRecord(
                JumpstartMatch.randomMatch(owned()),
                new JumpstartGameRecord("owned")
        );
    }

    private MatchAndRecord randomMixedMatch() {
        return new MatchAndRecord(
                new JumpstartMatch(randomMixedSetDeck(), randomMixedSetDeck()),
                new JumpstartGameRecord("mixed")
        );
    }

    private MatchAndRecord randomSingleSetMatch() {
        var set = randomSet();
        return new MatchAndRecord(
                JumpstartMatch.randomMatch(set),
                new JumpstartGameRecord(set.code())
        );
    }

    private MatchAndRecord doubleBoosterSingleSetMatch() {
        var set = randomSet();
        var b1 = set.randomBooster();
        var b2 = set.randomBooster();
        return new MatchAndRecord(
                new JumpstartMatch(new JumpstartDeck(b1, b1), new JumpstartDeck(b2, b2)),
                new JumpstartGameRecord(set.code())
        );
    }

    record MatchAndRecord(JumpstartMatch match, JumpstartGameRecord record) {
    }

    public JumpstartSet randomSet() {
        return jumpstartSets().get(random.nextInt(jumpstartSets().size()));
    }

    public JumpstartDeck randomMixedSetDeck() {
        return new JumpstartDeck(randomSet().randomBooster(), randomSet().randomBooster());
    }
}
