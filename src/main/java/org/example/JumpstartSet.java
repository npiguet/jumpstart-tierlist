package org.example;

import de.gesundkrank.jskills.GameInfo;
import forge.deck.Deck;
import forge.item.SealedTemplate;
import forge.item.generation.UnOpenedProduct;
import forge.util.storage.IStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class JumpstartSet {

    private static final Random RANDOM = new Random();
    private final String code;
    private final List<JumpstartBooster> boosters;

    public JumpstartSet(String code, IStorage<SealedTemplate> specialBoosters, GameInfo gameInfo) {
        this.code = code;
        this.boosters = specialBoosters.stream()
                .filter(template -> template.getName().startsWith(code))
                .map(template -> {
                    var cards = new UnOpenedProduct(template).get();
                    return new JumpstartBooster(code, template.getName(), cards, gameInfo);
                })
                .toList();
    }

    public JumpstartBooster randomBooster() {
        int index = RANDOM.nextInt(boosters.size());
        return boosters.get(index);
    }

    public JumpstartDeck randomDeck() {
        return new JumpstartDeck(randomBooster(), randomBooster());
    }

    public List<JumpstartBooster> boosters() {
        return boosters;
    }

    public String code(){
        return code;
    }

    public String toString() {
        return boosters.stream()
                .map(Objects::toString)
                .collect(Collectors.joining("\n", code + "\n", ""));
    }
}
