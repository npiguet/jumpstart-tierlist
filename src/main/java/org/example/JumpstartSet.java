package org.example;

import forge.item.SealedTemplate;
import forge.item.generation.UnOpenedProduct;
import forge.util.storage.IStorage;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class JumpstartSet {

    private static final Random RANDOM = new Random();
    private final String code;
    private final List<JumpstartBooster> boosters;

    private JumpstartSet(String code, List<JumpstartBooster> boosters) {
        this.code = code;
        this.boosters = boosters;
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

    public String code() {
        return code;
    }

    public String toString() {
        return boosters.stream()
                .map(Objects::toString)
                .collect(Collectors.joining("\n", code + "\n", ""));
    }

    public static JumpstartSet wotcSet(String code, IStorage<SealedTemplate> specialBoosters) {
        var boosters = specialBoosters.stream()
                .filter(template -> template.getName().startsWith(code))
                .map(template -> {
                    var cards = new UnOpenedProduct(template).get();
                    return new JumpstartBooster(code, template.getName(), cards);
                })
                .toList();

        return new JumpstartSet(code, boosters);
    }

    public static JumpstartSet cubeSet(String code, List<String> boosterNames, JumpstartEnvironment environment) {
        var boostersByName = environment.boosters().stream().collect(toMap(JumpstartBooster::name, Function.identity()));
        var boosters = boosterNames.stream()
                .map(boostersByName::get)
                .toList();
        return new JumpstartSet(code, boosters);
    }
}
