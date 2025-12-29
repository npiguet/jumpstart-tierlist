package org.example;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class JumpstartTierListSupervisor {
    private static final int PROCESS_COUNT = 12;
    private static final String JAVA_CMD = "java";
    private static final String WORKER_CLASS = JumpstartTierListPlayer.class.getName();

    private static final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    private static final List<Process> runningProcesses = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        // Shutdown hook to kill all workers
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Supervisor shutting down. Killing all workers...");
            shuttingDown.set(true);
            killAllProcesses();
        }));

        for (int i = 0; i < PROCESS_COUNT; i++) {
            int id = i;
            Thread monitor = new Thread(() -> monitorProcess(id));
            monitor.start();
        }

        // Keep supervisor alive
        Thread.currentThread().join();
    }

    private static void monitorProcess(int id) {
        while (!shuttingDown.get()) {
            try {
                Process process = startProcess(id);

                synchronized (runningProcesses) {
                    runningProcesses.add(process);
                }

                int exitCode = process.waitFor();

                synchronized (runningProcesses) {
                    runningProcesses.remove(process);
                }

                if (shuttingDown.get()) {
                    return;
                }

                System.out.println(
                        "Process " + id + " exited with code " + exitCode + ". Restarting..."
                );

                Thread.sleep(1000);

            } catch (Exception e) {
                if (!shuttingDown.get()) {
                    System.err.println("Monitor error for process " + id + ": " + e.getMessage());
                }
            }
        }
    }

    private static Process startProcess(int id) throws IOException {
        System.out.println("Starting process " + id);

        ProcessBuilder pb = new ProcessBuilder(
                JAVA_CMD,
                "-Xmx2048m",
                "-cp",
                System.getProperty("java.class.path"),
                WORKER_CLASS,
                String.valueOf(id)
        );

        pb.inheritIO();
        return pb.start();
    }

    private static void killAllProcesses() {
        synchronized (runningProcesses) {
            for (Process process : runningProcesses) {
                try {
                    process.destroy();
                    if (process.isAlive()) {
                        process.destroyForcibly();
                    }
                } catch (Exception ignored) {
                }
            }
            runningProcesses.clear();
        }
    }
}
