package org.example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
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

    public void monitor(Duration interval) {
        Thread fileMonitor = new Thread(() -> {
            long lastPosition = 0;
            Instant lastTime = Instant.now();

            while (true) {
                try {
                    Thread.sleep(interval.toMillis());

                    long newLines = 0;
                    long newPosition;

                    try (RandomAccessFile raf = new RandomAccessFile(getRecordFilePath().toFile(), "r")) {
                        raf.seek(lastPosition);

                        String line;
                        while ((line = raf.readLine()) != null) {
                            newLines++;
                        }

                        newPosition = raf.getFilePointer();
                    }

                    Instant now = Instant.now();
                    if (lastPosition > 0) {
                        double seconds = Duration.between(lastTime, now).toMillis() / 1000.0;

                        double rate = seconds > 0 ? newLines / seconds : 0;

                        System.out.printf(
                                "File %s: %.2f lines/sec (%d lines in %.2f sec)%n",
                                getRecordFilePath().getFileName(), rate, newLines, seconds
                        );
                    }

                    lastPosition = newPosition;
                    lastTime = now;

                } catch (Exception e) {
                    System.err.println("File monitor error: " + e.getMessage());
                }
            }
        });

        fileMonitor.setDaemon(true);
        fileMonitor.start();
    }
}
