package org.mtg.tierlist;

import java.io.File;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JumpstartGameRecord {

    private final String recordName;
    private final Path filePath;
    private final Predicate<JumpstartGameOutcome> outcomeFilter;

    public JumpstartGameRecord(String recordName) {
        this(recordName, null);
    }

    public JumpstartGameRecord(String recordName, Path filePath) {
        this(recordName, filePath, outcome -> true);
    }

    public JumpstartGameRecord(String recordName, Path folder, Predicate<JumpstartGameOutcome> outcomeFilter) {
        this.recordName = recordName;
        var path = Path.of("..", "jumpstart-tierlist", "records");
        if (folder != null) {
            path = path.resolve(folder);
        }
        this.filePath = path.resolve(recordName + ".csv");
        this.outcomeFilter = outcomeFilter;
    }

    public void append(JumpstartGameOutcome outcome) throws IOException {
        try (var writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
            writer.write(outcome.toCsv());
            writer.write('\n');
        }
    }

    public List<JumpstartGameOutcome> load(Collection<JumpstartBooster> allBoosters) throws IOException {
        var boostersByName = allBoosters.stream().collect(Collectors.toMap(
                JumpstartBooster::name,
                Function.identity()
        ));

        return Files.readAllLines(filePath).stream()
                .map(line -> JumpstartGameOutcome.fromCSV(line, boostersByName))
                .filter(outcomeFilter)
                .toList();
    }

    public void monitor(Duration interval) {
        Thread fileMonitor = new Thread(() -> {
            File file = filePath.toFile();
            Instant startTime = Instant.now();
            long newLinesSinceStart = 0;
            long lastPosition = file.length() - 1;

            while (true) {
                try {
                    Thread.sleep(interval.toMillis());

                    long newLines = 0;
                    long newPosition;

                    try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
                        raf.seek(lastPosition);

                        while (raf.readLine() != null) {
                            newLines++;
                        }

                        newPosition = raf.getFilePointer();
                    }

                    Instant now = Instant.now();
                    newLinesSinceStart += newLines;
                    double seconds = Duration.between(startTime, now).toMillis() / 1000.0;

                    double rate = seconds > 0 ? newLinesSinceStart / seconds : 0;

                    System.out.printf(
                            "File %s: %.2f lines/sec (%d lines in %.2f sec)%n",
                            filePath.getFileName(), rate, newLinesSinceStart, seconds
                    );

                    lastPosition = newPosition;

                } catch (Exception e) {
                    System.err.println("File monitor error: " + e.getMessage());
                }
            }
        });

        fileMonitor.setDaemon(true);
        fileMonitor.start();
    }

    public Path folderPath() {
        return this.filePath.getParent();
    }

    public String getName() {
        return recordName;
    }
}
