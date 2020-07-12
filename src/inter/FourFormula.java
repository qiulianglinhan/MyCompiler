package inter;

import common.SymbolTable;

import java.util.ArrayList;

/**
 * 四元式
 */
public class FourFormula {

    private final String op;      // 符号
    private final Object arg1;    // 参数一
    private final Object arg2;    // 参数二
    private Object result;  // 结果
    private static int line = 0;  // 存放生成四元式行号

    public FourFormula(String op, Object arg1,Object arg2, Object result){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public static void gen(String op, Object arg1,Object arg2, Object result){
        if (arg1 == null)
            arg1 = "_";
        if (arg2 == null)
            arg2 = "_";
        //System.out.println("["+op+","+arg1+","+arg2+","+result+"]");
        SymbolTable.fourFormulas.add(new FourFormula(op,arg1,arg2,result));
        line++; // add 1 automatically
    }

    public static int getLine() {
        return line;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "["+this.op+","+this.arg1+","+this.arg2+","+this.result+"]";
    }
}
