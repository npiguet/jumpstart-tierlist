package org.example;

import de.gesundkrank.jskills.GameInfo;
import forge.gui.GuiBase;
import forge.localinstance.properties.ForgePreferences;
import forge.model.FModel;

import java.util.List;

public abstract class JumpstartTierList {

    private List<JumpstartSet> sets;
    private GameInfo gameInfo;

    public JumpstartTierList() {
        initializeForgeEnvironment();
        var specialBoosters = FModel.getMagicDb().getSpecialBoosters();
        this.gameInfo = GameInfo.getDefaultGameInfo();
        this.sets = List.of(
                new JumpstartSet("JMP", specialBoosters, gameInfo),
                new JumpstartSet("J22", specialBoosters, gameInfo),
                new JumpstartSet("J25", specialBoosters, gameInfo)
        );
    }

    protected List<JumpstartSet> jumpstartSets() {
        return sets;
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
