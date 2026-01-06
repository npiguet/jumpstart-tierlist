package org.mtg.tierlist;

import forge.gui.GuiBase;
import forge.localinstance.properties.ForgePreferences;
import forge.model.FModel;

import java.util.List;

public class WotcJumpstartEnvironment implements JumpstartEnvironment {

    static {
        GuiBase.setInterface(new GuiHeadless("..\\forge\\forge-gui\\"));
        FModel.initialize(null, preferences -> {
            preferences.setPref(ForgePreferences.FPref.LOAD_CARD_SCRIPTS_LAZILY, false);
            preferences.setPref(ForgePreferences.FPref.UI_LANGUAGE, "en-US");
            return null;
        });
    }

    private final List<JumpstartSet> sets;

    public WotcJumpstartEnvironment() {
        var specialBoosters = FModel.getMagicDb().getSpecialBoosters();
        this.sets = List.of(
                JumpstartSet.wotcSet("JMP", specialBoosters),
                JumpstartSet.wotcSet("J22", specialBoosters),
                JumpstartSet.wotcSet("J25", specialBoosters)
        );
    }

    @Override
    public List<JumpstartSet> sets() {
        return sets;
    }
}
