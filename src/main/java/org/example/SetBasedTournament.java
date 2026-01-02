package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetBasedTournament implements Tournament {

    private static final Random RANDOM = new Random();

    private final List<JumpstartSet> sets;
    private final Path folder;
    private final Function<List<JumpstartSet>, JumpstartDeck> deckSelector;
    private final Function<List<JumpstartSet>, List<JumpstartSet>> setPerMatchSelector;

    private SetBasedTournament(List<JumpstartSet> sets,
                               String folder,
                               Function<List<JumpstartSet>, List<JumpstartSet>> setPerMatchSelector,
                               Function<List<JumpstartSet>, JumpstartDeck> deckSelector) {
        this.sets = sets;
        this.folder = Path.of(folder);
        this.setPerMatchSelector = setPerMatchSelector;
        this.deckSelector = deckSelector;
    }

    @Override
    public JumpstartGameOutcome playAndRecordNextMatch() throws IOException {
        var selectedSets = setPerMatchSelector.apply(sets);
        var deck1 = deckSelector.apply(selectedSets);
        var deck2 = deckSelector.apply(selectedSets);
        var match = new JumpstartMatch(deck1, deck2);
        var outcome = match.play();
        if (outcome != null) {
            var recordName = selectedSets.stream().map(JumpstartSet::code).sorted().collect(Collectors.joining("-"));
            var record = new JumpstartGameRecord(recordName, folder);
            record.append(outcome);
        }
        return outcome;
    }

    @Override
    public List<JumpstartGameRecord> getRecords() {
        // oof, kinda ugly, but it works
        var selected = setPerMatchSelector.apply(sets);
        if (selected.size() == 1) {
            return sets.stream().map(s -> new JumpstartGameRecord(s.code(), folder)).toList();
        }

        var recordName = selected.stream().map(JumpstartSet::code).sorted().collect(Collectors.joining("-"));
        return List.of(new JumpstartGameRecord(recordName, folder));
    }

    public static SetBasedTournament withRandomBoosters(JumpstartEnvironment environment, boolean mixedSets) {
        return new SetBasedTournament(
                environment.sets(),
                "random",
                mixedSets ? SetBasedTournament::mixedSetsPerMatch : SetBasedTournament::singleSetPerMatch,
                SetBasedTournament::randomBoosterDeck
        );
    }

    public static SetBasedTournament withDoubleBoosters(JumpstartEnvironment environment, boolean mixedSets) {
        return new SetBasedTournament(
                environment.sets(),
                "double",
                mixedSets ? SetBasedTournament::mixedSetsPerMatch : SetBasedTournament::singleSetPerMatch,
                SetBasedTournament::doubleBoosterDeck
        );
    }

    private static JumpstartDeck randomBoosterDeck(List<JumpstartSet> sets) {
        var b1 = sets.get(RANDOM.nextInt(sets.size())).randomBooster();
        var b2 = sets.get(RANDOM.nextInt(sets.size())).randomBooster();
        return new JumpstartDeck(b1, b2);
    }

    private static JumpstartDeck doubleBoosterDeck(List<JumpstartSet> sets) {
        var set = sets.get(RANDOM.nextInt(sets.size()));
        var booster = set.randomBooster();
        return new JumpstartDeck(booster, booster);
    }

    private static List<JumpstartSet> singleSetPerMatch(List<JumpstartSet> sets) {
        return List.of(sets.get(RANDOM.nextInt(sets.size())));
    }

    private static List<JumpstartSet> mixedSetsPerMatch(List<JumpstartSet> sets) {
        return sets;
    }
}
