package inter;

import common.SymbolTable;

/**
 * 四元式
 */
public class FourFormula {

    private final String op;      // 符号
    private final String arg1;    // 参数一
    private final String arg2;    // 参数二
    private String result;  // 结果
    private static int line = 0;  // 存放生成四元式行号

    public FourFormula(String op, String arg1,String arg2, String result){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public static void gen(String op, String arg1,String arg2, String result){
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

    public String getResult() {
        return result;
    }

    public String getOp() {
        return op;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "["+this.op+","+this.arg1+","+this.arg2+","+this.result+"]";
    }
}
