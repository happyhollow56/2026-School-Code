import java.io.*;
import java.util.*;

public class CheckGroupingSymbols {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CheckGroupingSymbols <filename>");
            return;
        }
        String filename = args[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            Stack<Character> stack = new Stack<>();
            String line;
            int lineNum = 0;
            boolean correct = true;
            while ((line = br.readLine()) != null) {
                lineNum++;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '(' || c == '{' || c == '[') {
                        stack.push(c);
                    } else if (c == ')' || c == '}' || c == ']') {
                        if (stack.isEmpty()) {
                            System.out.println("Unmatched closing symbol '" + c + "' at line " + lineNum + ", position " + (i + 1));
                            correct = false;
                            break;
                        }
                        char open = stack.pop();
                        if (!matches(open, c)) {
                            System.out.println("Mismatched symbol '" + c + "' at line " + lineNum + ", position " + (i + 1) + ", expected for '" + open + "'");
                            correct = false;
                            break;
                        }
                    }
                }
                if (!correct) break;
            }
            br.close();
            if (correct && stack.isEmpty()) {
                System.out.println("Grouping symbols are correctly paired.");
            } else if (correct) {
                System.out.println("Unmatched opening symbols remaining.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')') ||
               (open == '{' && close == '}') ||
               (open == '[' && close == ']');
    }
}
