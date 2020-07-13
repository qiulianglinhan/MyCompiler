import LL1.RecursiveDescent;
import lexer.LexicalAnalysis;
import run.Result;
import run.RunFourFormula;

import java.io.IOException;

public class ApplicationRun {
    public static void main(String[] args) throws IOException {
        String fileName = "src\\test.c";
        new LexicalAnalysis(fileName);
        new RecursiveDescent(true);
        new RunFourFormula();
        System.out.println(Result.RESULT);
    }
}
