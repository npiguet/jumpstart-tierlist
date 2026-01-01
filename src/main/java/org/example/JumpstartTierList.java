package org.example;

import de.gesundkrank.jskills.GameInfo;
import forge.gui.GuiBase;
import forge.localinstance.properties.ForgePreferences;
import forge.model.FModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class JumpstartTierList {

    private final List<JumpstartSet> sets;
    private final JumpstartSet cube;
    private final JumpstartSet owned;
    private final GameInfo gameInfo;

    public JumpstartTierList() {
        initializeForgeEnvironment();
        var specialBoosters = FModel.getMagicDb().getSpecialBoosters();
        this.gameInfo = GameInfo.getDefaultGameInfo();
        this.sets = List.of(
                JumpstartSet.wotcSet("JMP", specialBoosters, gameInfo),
                JumpstartSet.wotcSet("J22", specialBoosters, gameInfo),
                JumpstartSet.wotcSet("J25", specialBoosters, gameInfo)
        );

        var cubeList = List.of(
                "JMP Angels 2",
                "JMP Devilish 4",
                "JMP Dinosaurs 2",
                "JMP Doctor 1",
                "JMP Dogs 1",
                "JMP Feathered Friends 3",
                "JMP Garruk",
                "JMP Heavily Armored 3",
                "JMP Liliana",
                "JMP Minions 2",
                "JMP Minotaurs 1",
                "JMP Seismic",
                "JMP Smashing 2",
                "JMP Spooky 1",
                "JMP Tree Hugging 4",
                "JMP Under the Sea 2",
                "JMP Well-Read 3",
                "JMP Wizards 2",
                "J22 Blink 4",
                "J22 Demons 2",
                "J22 Detective 1",
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
                "J22 Raid 4",
                "J22 Scrying 1",
                "J22 Teamwork 2",
                "J22 Think Again 3",
                "J22 Treasure 4",
                "J22 Vehicles",
                "J22 Zombies 1",
                "JMP Phyrexian",
                "J22 Unlucky Thirteen",
                "J22 Rats",
                "J22 Morbid 2",
                "JMP Discarding 1",
                "J22 Primates",
                "J22 Eldrazi",
                "J22 Multi-Headed 2",
                "J22 Speedy",
                "JMP Teferi",
                "J22 Snow",
                "JMP Unicorns"
        );
        this.cube = JumpstartSet.cubeSet("cube", cubeList, specialBoosters, gameInfo );

        var extraOwned = List.of(
                "JMP Devilish 3",
                "JMP Dinosaurs 1",
                "JMP Goblins 3",
                "JMP Tree Hugging 2",
                "JMP Under the Sea 1",
                "JMP Wizards 4",
                "J22 Goblins 1",
                "J22 Merfolk 1",
                "J22 Teamwork 1",
                "J22 Think Again 1",
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
                "J25 Vampires 4"
        );
        var listOwned = new ArrayList<>(cubeList);
        listOwned.addAll(extraOwned);
        this.owned = JumpstartSet.cubeSet("owned", listOwned, specialBoosters, gameInfo);
    }

    protected List<JumpstartSet> jumpstartSets() {
        return sets;
    }

    protected JumpstartSet cube() {
        return cube;
    }

    protected JumpstartSet owned() {
        return owned;
    }

    protected GameInfo gameInfo() {
        return gameInfo;
    }

    private static void initializeForgeEnvironment() {
        GuiBase.setInterface(new GuiHeadless("..\\forge\\forge-gui\\"));
        FModel.initialize(null, preferences -> {
            preferences.setPref(ForgePreferences.FPref.LOAD_CARD_SCRIPTS_LAZILY, false);
            preferences.setPref(ForgePreferences.FPref.UI_LANGUAGE, "en-US");
            return null;
        });
    }
}