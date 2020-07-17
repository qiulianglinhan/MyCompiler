package inter;

import common.SymbolTable;

import java.util.Stack;

public class Break {

    private Stack<Integer> breakStack;

    public Break(){
        breakStack = new Stack<>();
    }

    public Stack<Integer> getBreakStack() {
        return breakStack;
    }

    /**
     * 生成break语句四元式
     * @return 生成四元式的索引
     */
    public static int genBreakCode(){
        FourFormula.gen("goto",null,null,"?");
        return FourFormula.getLine()-1;
    }

    /**
     * 回填函数
     * @param curLine 需要回填 goto 语句的行号
     */
    public static void backPatchBreakCode(int curLine){
        SymbolTable.fourFormulas.get(curLine).setResult(String.valueOf(FourFormula.getLine()));
    }

}
