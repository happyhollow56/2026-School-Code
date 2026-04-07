import java.io.*;
import java.util.*;

public class KeywordCounter {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
        "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
        "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
        "volatile", "while"
    ));

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java KeywordCounter <filename>");
            return;
        }
        String filename = args[0];
        try {
            String content = readFile(filename);
            int count = countKeywords(content);
            System.out.println("Number of keywords: " + count);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static String readFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static int countKeywords(String content) {
        int count = 0;
        int i = 0;
        int length = content.length();
        StringBuilder word = new StringBuilder();
        boolean inString = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        while (i < length) {
            char c = content.charAt(i);
            if (inBlockComment) {
                if (c == '*' && i + 1 < length && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                    continue;
                }
            } else if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                }
            } else if (inString) {
                if (c == '"' && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inString = false;
                }
            } else {
                if (c == '/' && i + 1 < length) {
                    if (content.charAt(i + 1) == '/') {
                        inLineComment = true;
                        i += 2;
                        continue;
                    } else if (content.charAt(i + 1) == '*') {
                        inBlockComment = true;
                        i += 2;
                        continue;
                    }
                } else if (c == '"') {
                    inString = true;
                } else if (Character.isLetter(c) || c == '_') {
                    word.setLength(0);
                    while (i < length && (Character.isLetterOrDigit(content.charAt(i)) || content.charAt(i) == '_')) {
                        word.append(content.charAt(i));
                        i++;
                    }
                    if (KEYWORDS.contains(word.toString())) {
                        count++;
                    }
                    continue;
                }
            }
            i++;
        }
        return count;
    }
}
