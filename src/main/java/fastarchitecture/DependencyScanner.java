package fastarchitecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Scans Java source trees and extracts import statements and class structures without bytecode or reflection.
 */
public final class DependencyScanner {

    public record ImportStatement(int lineNumber, String target, String rawLine) {}

    public record ParsedSource(Path file, String packageName, String className, List<ImportStatement> imports) {}

    public List<Path> discoverSourceFiles(Path root) throws IOException {
        List<Path> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                  .filter(p -> p.toString().endsWith(".java"))
                  .filter(p -> !isIgnored(p))
                  .forEach(files::add);
        }
        return files;
    }

    public ParsedSource parseSource(Path file) throws IOException {
        String pkg = "";
        String simpleName = file.getFileName().toString();
        if (simpleName.endsWith(".java")) {
            simpleName = simpleName.substring(0, simpleName.length() - 5);
        }

        List<ImportStatement> imports = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);
        int lineNum = 0;
        for (String raw : lines) {
            lineNum++;
            String line = raw.trim();
            if (line.startsWith("package ")) {
                pkg = line.substring("package ".length()).replace(";", "").trim();
            } else if (line.startsWith("import ")) {
                String imp = line.substring("import ".length()).replace(";", "").trim();
                if (imp.startsWith("static ")) {
                    imp = imp.substring("static ".length()).trim();
                }
                imports.add(new ImportStatement(lineNum, imp, line));
            }
        }
        return new ParsedSource(file, pkg, simpleName, imports);
    }

    private boolean isIgnored(Path path) {
        String s = path.toString();
        return s.contains(".git") || s.contains("target") || s.contains("build") || s.contains(".idea");
    }
}
