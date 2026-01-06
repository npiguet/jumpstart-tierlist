package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CubeRandomTournament implements Tournament {

    private static final List<String> MY_OWNED_BOOSTER_NAMES = List.of(
            "J22 Blink 4",
            "J22 Demons 2",
            "J22 Detective 1",
            "J22 Eldrazi",
            "J22 Elves 3",
            "J22 Experimental 2",
            "J22 Ferocious 4",
            "J22 Fiery 3",
            "J22 Go to School 1",
            "J22 Goblins 1",
            "J22 Goblins 4",
            "J22 Insects 2",
            "J22 Landfall 2",
            "J22 Law 4",
            "J22 Merfolk 1",
            "J22 Merfolk 2",
            "J22 Morbid 2",
            "J22 Multi-Headed 2",
            "J22 Primates",
            "J22 Raid 4",
            "J22 Rats",
            "J22 Scrying 1",
            "J22 Snow",
            "J22 Speedy",
            "J22 Teamwork 1",
            "J22 Teamwork 2",
            "J22 Think Again 1",
            "J22 Think Again 3",
            "J22 Treasure 4",
            "J22 Unlucky Thirteen",
            "J22 Vehicles",
            "J22 Zombies 1",
            "J25 Angels 1",
            "J25 Armed 2",
            "J25 Bloody 2",
            "J25 Bookworms 3",
            "J25 Clerics 3",
            "J25 Dinner",
            "J25 Dragons 1",
            "J25 Drowned 2",
            "J25 Enchanted 2",
            "J25 Enchanted 3",
            "J25 Explorers 1",
            "J25 Goblins 1",
            "J25 Grave Robbers 4",
            "J25 Icky 2",
            "J25 Illusions",
            "J25 Inventive 4",
            "J25 Landfall 3",
            "J25 Nefarious",
            "J25 Prideful",
            "J25 Soaring 2",
            "J25 Stalwart 1",
            "J25 Vampires 4",
            "JMP Angels 2",
            "JMP Devilish 3",
            "JMP Devilish 4",
            "JMP Dinosaurs 1",
            "JMP Dinosaurs 2",
            "JMP Discarding 1",
            "JMP Doctor 1",
            "JMP Dogs 1",
            "JMP Feathered Friends 3",
            "JMP Garruk",
            "JMP Goblins 3",
            "JMP Heavily Armored 3",
            "JMP Liliana",
            "JMP Minions 2",
            "JMP Minotaurs 1",
            "JMP Phyrexian",
            "JMP Seismic",
            "JMP Smashing 2",
            "JMP Spooky 1",
            "JMP Teferi",
            "JMP Tree Hugging 2",
            "JMP Tree Hugging 4",
            "JMP Under the Sea 1",
            "JMP Under the Sea 2",
            "JMP Unicorns",
            "JMP Well-Read 3",
            "JMP Wizards 2",
            "JMP Wizards 4"
    );

    private static final List<String> MY_CUBE_BOOSTER_NAMES = List.of(
            "J22 Blink 4",
            "J22 Demons 2",
            "J22 Detective 1",
            "J22 Eldrazi",
            "J22 Elves 3",
            "J22 Experimental 2",
            "J22 Ferocious 4",
            "J22 Fiery 3",
            "J22 Go to School 1",
            "J22 Goblins 4",
            "J22 Insects 2",
            "J22 Landfall 2",
            "J22 Law 4",
            "J22 Merfolk 2",
            "J22 Morbid 2",
            "J22 Multi-Headed 2",
            "J22 Primates",
            "J22 Raid 4",
            "J22 Rats",
            "J22 Scrying 1",
            "J22 Snow",
            "J22 Speedy",
            "J22 Teamwork 2",
            "J22 Think Again 3",
            "J22 Treasure 4",
            "J22 Unlucky Thirteen",
            "J22 Vehicles",
            "J22 Zombies 1",
            "JMP Angels 2",
            "JMP Devilish 4",
            "JMP Dinosaurs 2",
            "JMP Discarding 1",
            "JMP Doctor 1",
            "JMP Dogs 1",
            "JMP Feathered Friends 3",
            "JMP Garruk",
            "JMP Heavily Armored 3",
            "JMP Liliana",
            "JMP Minions 2",
            "JMP Minotaurs 1",
            "JMP Phyrexian",
            "JMP Seismic",
            "JMP Smashing 2",
            "JMP Spooky 1",
            "JMP Teferi",
            "JMP Tree Hugging 4",
            "JMP Under the Sea 2",
            "JMP Unicorns",
            "JMP Well-Read 3",
            "JMP Wizards 2"
    );

    private static final List<String> MY_CUBE_V2_BOOSTER_NAMES = List.of(
            "J25 Angels 1",
            "J25 Prideful",
            "J25 Enchanted 3",
            "J25 Armed 2",
            "J25 Stalwart 1",
            "JMP Heavily Armored 3",
            "J22 Blink 4",
            "JMP Doctor 1",
            "J22 Vehicles",
            "JMP Feathered Friends 3",
            "J22 Merfolk 2",
            "J25 Soaring 2",
            "J25 Illusions",
            "JMP Teferi",
            "J25 Inventive 4",
            "JMP Wizards 2",
            "J22 Go to School 1",
            "J22 Think Again 3",
            "JMP Well-Read 3",
            "J22 Detective 1",
            "JMP Discarding 1",
            "J25 Vampires 4",
            "J25 Grave Robbers 4",
            "J25 Icky 2",
            "J22 Rats",
            "J25 Nefarious",
            "J22 Demons 2",
            "J22 Morbid 2",
            "J22 Zombies 1",
            "J25 Clerics 3",
            "JMP Goblins 3",
            "J25 Bloody 2",
            "J25 Dragons 1",
            "JMP Smashing 2",
            "JMP Minotaurs 1",
            "J22 Raid 4",
            "J22 Treasure 4",
            "JMP Devilish 4",
            "J22 Experimental 2",
            "J22 Fiery 3",
            "JMP Dinosaurs 1",
            "JMP Tree Hugging 4",
            "J25 Landfall 3",
            "J22 Ferocious 4",
            "JMP Garruk",
            "J22 Multi-Headed 2",
            "J25 Explorers 1",
            "J22 Elves 3",
            "J25 Dinner",
            "J22 Primates"
    );

    private final JumpstartSet cubeSet;
    private final JumpstartGameRecord record;

    private CubeRandomTournament(String cubeName, List<String> boosterNames, JumpstartEnvironment environment) {
        this.record = new JumpstartGameRecord(cubeName, Path.of("cubes"), JumpstartGameOutcome::hasNoDuplicateBoosters);
        this.cubeSet = JumpstartSet.cubeSet("cube", boosterNames, environment);
    }

    @Override
    public JumpstartGameOutcome playAndRecordNextMatch() throws IOException {
        var match = JumpstartMatch.randomMatch(cubeSet);
        var outcome = match.play();
        if (outcome != null) {
            record.append(outcome);
        }
        return outcome;
    }

    @Override
    public List<JumpstartGameRecord> getRecords() {
        return List.of(record);
    }

    public static CubeRandomTournament withMyCube(JumpstartEnvironment environment) {
        return new CubeRandomTournament("cube", MY_CUBE_BOOSTER_NAMES, environment);
    }

    public static CubeRandomTournament withMyOwnedBoosters(JumpstartEnvironment environment) {
        return new CubeRandomTournament("owned", MY_OWNED_BOOSTER_NAMES, environment);
    }
    public static CubeRandomTournament withMyCubeV2(JumpstartEnvironment environment) {
        return new CubeRandomTournament("cube-v2", MY_CUBE_V2_BOOSTER_NAMES, environment);
    }

}
