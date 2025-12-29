package org.example;

import java.io.IOException;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class JumpstartTierListPlayer extends JumpstartTierList {
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        // the game AI is pretty unstable, and often times out or otherwise requires the program to be killed.
        // So we'll separate playing games from calculating rating results. And we'll accumulate results for games
        // over multiple runs

        var player = new JumpstartTierListPlayer();
        player.playGames(100_000);
    }

    public void playGames(int numberOfGames) throws IOException {
        for (int i = 0; i < numberOfGames; i++) {
            var set = jumpstartSets().get(random.nextInt(jumpstartSets().size()));
            var setRecord = new JumpstartGameRecord(set.code());
            var match = JumpstartMatch.randomMatch(set);
            var outcome = match.play();
            if (outcome != null) {
                setRecord.append(outcome);
                System.out.println("Game " + (i + 1) + ": " + outcome);
            }
        }
    }
}
