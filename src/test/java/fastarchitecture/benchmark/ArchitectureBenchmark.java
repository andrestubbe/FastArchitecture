package fastarchitecture.benchmark;

import fastarchitecture.ArchitectureManifest;
import fastarchitecture.ArchitectureReport;
import fastarchitecture.ArchitectureValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchitectureBenchmark {

    public static void main(String[] args) throws IOException {
        System.out.println("=================================================");
        System.out.println(" FastArchitecture Empirical Benchmark Harness");
        System.out.println(" OS:  " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
        System.out.println(" JVM: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        System.out.println(" CPUs: " + Runtime.getRuntime().availableProcessors());
        System.out.println("=================================================");

        // Setup synthetic 100-class project
        Path tempDir = Files.createTempDirectory("fastarch_bench_100");
        try {
            Path modelDir = tempDir.resolve("src/com/bench/model");
            Path controlDir = tempDir.resolve("src/com/bench/control");
            Path viewDir = tempDir.resolve("src/com/bench/view");
            Files.createDirectories(modelDir);
            Files.createDirectories(controlDir);
            Files.createDirectories(viewDir);

            // 40 Model classes
            for (int i = 0; i < 40; i++) {
                String src = "package com.bench.model;\npublic class State" + i + " { int value = " + i + "; }\n";
                Files.writeString(modelDir.resolve("State" + i + ".java"), src);
            }
            // 30 Control classes importing Model
            for (int i = 0; i < 30; i++) {
                String src = "package com.bench.control;\nimport com.bench.model.State0;\nimport com.bench.model.State1;\npublic class Controller" + i + " { void update() {} }\n";
                Files.writeString(controlDir.resolve("Controller" + i + ".java"), src);
            }
            // 30 View classes importing Model
            for (int i = 0; i < 30; i++) {
                String src = "package com.bench.view;\nimport com.bench.model.State0;\npublic class Widget" + i + " { void render() {} }\n";
                Files.writeString(viewDir.resolve("Widget" + i + ".java"), src);
            }

            // Create architecture.fca
            String fca = "architecture FastArchitecture\nModel owns state\nControl reads Model\nControl writes Model\nView reads Model\ndeny Model -> Control\ndeny Model -> View\ndeny View -> Control\n";
            Files.writeString(tempDir.resolve("architecture.fca"), fca);

            ArchitectureManifest manifest = ArchitectureManifest.parse(tempDir.resolve("architecture.fca"));
            ArchitectureValidator validator = new ArchitectureValidator(manifest);

            // Warmup (50 iterations)
            for (int i = 0; i < 50; i++) {
                validator.validate(tempDir);
            }

            // Measurement (100 iterations)
            int runs = 100;
            List<Double> timesMs = new ArrayList<>();
            for (int i = 0; i < runs; i++) {
                long start = System.nanoTime();
                ArchitectureReport report = validator.validate(tempDir);
                long elapsed = System.nanoTime() - start;
                if (!report.isValid()) {
                    throw new IllegalStateException("Validation failed in benchmark!");
                }
                timesMs.add(elapsed / 1_000_000.0);
            }

            Collections.sort(timesMs);
            double min = timesMs.get(0);
            double p50 = timesMs.get(timesMs.size() / 2);
            double p95 = timesMs.get((int) (timesMs.size() * 0.95));
            double max = timesMs.get(timesMs.size() - 1);
            double avg = timesMs.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            System.out.printf("Results across %d runs on 100 classes:\n", runs);
            System.out.printf("  Min: %.2f ms\n", min);
            System.out.printf("  P50: %.2f ms\n", p50);
            System.out.printf("  Avg: %.2f ms\n", avg);
            System.out.printf("  P95: %.2f ms\n", p95);
            System.out.printf("  Max: %.2f ms\n", max);
            System.out.println("=================================================");

        } finally {
            // cleanup tempDir
            try (var walk = Files.walk(tempDir)) {
                walk.sorted((a, b) -> b.compareTo(a)).forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (Exception ignored) {}
                });
            }
        }
    }
}