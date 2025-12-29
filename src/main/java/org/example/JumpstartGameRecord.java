package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JumpstartGameRecord {

    private final String setCode;

    public JumpstartGameRecord(String setCode) {
        this.setCode = setCode;
    }

    private Path getRecordFilePath() {
        return Path.of("..", "jumpstart-tierlist", "records", setCode + ".csv");
    }

    public void append(JumpstartGameOutcome outcome) throws IOException {
        var path = getRecordFilePath();
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
            writer.write(outcome.toCsv());
            writer.write('\n');
        }
    }

    public List<JumpstartGameOutcome> load(Collection<JumpstartBooster> allBoosters) throws IOException {
        var boostersByName = allBoosters.stream().collect(Collectors.toMap(
                JumpstartBooster::name,
                Function.identity()
        ));

        return Files.readAllLines(getRecordFilePath()).stream()
                .map(line -> JumpstartGameOutcome.fromCSV(line, boostersByName))
                .toList();
    }
}
