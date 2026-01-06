package org.mtg.tierlist;

import java.io.IOException;
import java.util.Random;

public class WorkerApp {
    private static final Random random = new Random();

    private final Tournament tournament;
    private final int numberOfGames;

    public WorkerApp(Tournament tournament, int numberOfGames) {
        this.tournament = tournament;
        this.numberOfGames = numberOfGames;
    }

    public static void main(String[] args) throws IOException {
        // the game AI is pretty unstable, and often times out or otherwise requires the program to be killed.
        // So we'll separate playing games from calculating rating results. And we'll accumulate results for games
        // over multiple runs

        var environment = new WotcJumpstartEnvironment();

//        var tournament = CubeRandomTournament.withMyOwnedBoosters(environment);
//        var tournament = CubeRandomTournament.withMyCube(environment);
        var tournament = SetBasedTournament.withRandomBoosters(environment, false);
//        var tournament = SetBasedTournament.withDoubleBoosters(environment, true);

        var worker = new WorkerApp(tournament, 100_000);
        worker.playTournament();
    }

    public void playTournament() throws IOException {
        for (int i = 0; i < numberOfGames; i++) {
            var outcome = tournament.playAndRecordNextMatch();
            if (outcome != null) {
                System.out.println("Game " + (i + 1) + ": " + outcome);
            }
        }
    }
}
