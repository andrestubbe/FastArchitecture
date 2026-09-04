package fastarchitecture.cli;

import fastarchitecture.ArchitectureReport;
import fastarchitecture.FastArchitecture;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command-Line Interface for FastArchitecture validation and skeleton generation.
 */
public final class Main {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("-h")) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();
        try {
            switch (command) {
                case "check", "validate" -> {
                    Path target = args.length > 1 ? Paths.get(args[1]) : Paths.get(".");
                    ArchitectureReport report = FastArchitecture.validate(target);
                    System.out.println(report.toFormattedString());
                    if (!report.isValid()) {
                        System.exit(1);
                    }
                }
                case "init", "create" -> {
                    String appName = args.length > 1 ? args[1] : "MyApp";
                    Path target = args.length > 2 ? Paths.get(args[2]) : Paths.get(".");
                    FastArchitecture.initSkeleton(target, appName);
                    System.out.println("✓ FastArchitecture skeleton successfully created for: " + appName);
                    System.out.println("  Manifest: " + target.resolve("architecture.fca").toAbsolutePath());
                    System.out.println("  Layers:   Model/, Control/, View/");
                }
                default -> {
                    System.err.println("Unknown command: " + command);
                    printUsage();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

    private static void printUsage() {
        System.out.println("""
                FastArchitecture CLI v0.1.0 — Architecture Standard for Human + AI Engineering

                Usage:
                  fastarchitecture check [path]       Validate project architecture against architecture.fca
                  fastarchitecture init [name] [dir]  Scaffold compliant Model / Control / View skeleton

                Examples:
                  java -jar FastArchitecture.jar check .
                  java -jar FastArchitecture.jar init ScreenZoom .
                """);
    }
}
