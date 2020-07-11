import LL1.RecursiveDescent;
import lexer.LexicalAnalysis;

import java.io.IOException;

public class ApplicationRun {
    public static void main(String[] args) throws IOException {
        String fileName = "src\\test.c";
        new LexicalAnalysis(fileName);
        new RecursiveDescent();
    }
}
