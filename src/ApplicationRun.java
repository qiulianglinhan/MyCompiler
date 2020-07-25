import LL1.RecursiveDescent;
import lexer.LexicalAnalysis;
import run.Result;
import run.RunFourFormula;

import java.io.IOException;

public class ApplicationRun {
    public static void main(String[] args) throws IOException {
        String fileName = "test\\FunctionTest\\MaxFunctionTest.c";
        new LexicalAnalysis(fileName);
        new RecursiveDescent(true);
        new RunFourFormula(true,fileName);
        System.out.println(Result.RESULT);
        System.out.println(Result.ARRAYRESULT);
    }
}
