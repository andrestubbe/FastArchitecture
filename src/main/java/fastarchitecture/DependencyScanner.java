package fastarchitecture;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Scans Java source trees and extracts import statements and inline qualified class usages.
 */
public final class DependencyScanner {

    private static final Pattern QUALIFIED_TYPE_PATTERN = 
        Pattern.compile("\\b([a-zA-Z0-9_]+\\.(model|control|controller|view)\\.[a-zA-Z0-9_]+)\\b");

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
        int lineNum = 0;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String raw;
            while ((raw = reader.readLine()) != null) {
                lineNum++;
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")) {
                    continue;
                }
                if (line.startsWith("package ")) {
                    pkg = line.substring("package ".length()).replace(";", "").trim();
                } else if (line.startsWith("import ")) {
                    String imp = line.substring("import ".length()).replace(";", "").trim();
                    if (imp.startsWith("static ")) {
                        imp = imp.substring("static ".length()).trim();
                    }
                    imports.add(new ImportStatement(lineNum, imp, line));
                } else {
                    // Check for inline fully-qualified type references
                    Matcher matcher = QUALIFIED_TYPE_PATTERN.matcher(line);
                    while (matcher.find()) {
                        String matchedType = matcher.group(1);
                        imports.add(new ImportStatement(lineNum, matchedType, line));
                    }
                }
            }
        }
        return new ParsedSource(file, pkg, simpleName, imports);
    }

    private boolean isIgnored(Path path) {
        String s = path.toString();
        return s.contains(".git") || s.contains("target") || s.contains("build") || s.contains(".idea");
    }
}