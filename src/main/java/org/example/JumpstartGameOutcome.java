package org.example;

import de.gesundkrank.jskills.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;

public record JumpstartGameOutcome(JumpstartDeck winner, JumpstartDeck loser, Duration duration, int turnCount) {

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

    public boolean hasNoDuplicateBoosters() {
        var boosters = new HashSet<JumpstartBooster>();
        boosters.addAll(winner.getBoosters());
        boosters.addAll(loser.getBoosters());
        return boosters.size() == 4;
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
