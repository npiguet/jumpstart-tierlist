package org.example;

import de.gesundkrank.jskills.GameInfo;
import forge.gui.GuiBase;
import forge.localinstance.properties.ForgePreferences;
import forge.model.FModel;

import java.util.Map;

/**
 * Hello world!
 *
 */
public class JumpstartTierList {

    public static void main(String[] args) {

        initializeForgeEnvironment();
        var specialBoosters = FModel.getMagicDb().getSpecialBoosters();
        var gameInfo = GameInfo.getDefaultGameInfo();
        var jumpstartSets = Map.of(
                "JMP", new JumpstartSet("JMP", specialBoosters, gameInfo),
                "J22", new JumpstartSet("J22", specialBoosters, gameInfo),
                "J25", new JumpstartSet("J25", specialBoosters, gameInfo)
        );
        System.out.println(jumpstartSets);

        var jmp = jumpstartSets.get("JMP");

        for (int i = 0; i < 1000; i++) {
            var match = JumpstartMatch.randomMatch(jmp);
            var outcome = match.play();
            outcome.updateRatings(gameInfo);
            System.out.println(outcome);
        }

//        List<CompletableFuture<JumpstartGameOutcome>> outcomeFutures = new ArrayList<>();
//        for (int i = 0; i < 100; i ++) {
//            outcomeFutures.add(CompletableFuture.supplyAsync(() -> {
//                var match = JumpstartMatch.randomMatch(jmp);
//                var outcome = match.play();
//                System.out.println(outcome);
//                return outcome;
//            }));
//        }
//
//        outcomeFutures.stream().forEach(CompletableFuture::join);
//
//        outcomeFutures.stream().forEach(future -> {
//            future.join().updateRatings(gameInfo);
//        });

        jmp.boosters().stream()
                .sorted(JumpstartBooster.bestRatingFirst())
                .forEach(b -> {
                    System.out.println(b.name() + ": " + b.rating());
                });
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
