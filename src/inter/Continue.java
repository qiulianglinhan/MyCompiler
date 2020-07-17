package inter;

import common.SymbolTable;

import java.util.Stack;

public class Continue {

    private Stack<Integer> continueStack;

    public Continue(){
        continueStack = new Stack<>();
    }

    public Stack<Integer> getContinueStack() {
        return continueStack;
    }

    /**
     * 生成continue语句四元式
     * @return 生成continue语句的行号
     */
    public static int genContinueCode(){
        FourFormula.gen("goto",null,null,"?");
        return FourFormula.getLine()-1;
    }

    /**
     * 回填函数
     * @param curLine 需要回填 goto 语句的行号
     * @param changeLine 循环自增行号
     */
    public static void backPatchContinueCode(int curLine,int changeLine){
        SymbolTable.fourFormulas.get(curLine).setResult(String.valueOf(changeLine));
    }
}
