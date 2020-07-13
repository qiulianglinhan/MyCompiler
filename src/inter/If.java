package inter;

import common.SymbolTable;

public class If {

    /**
     * 生成 if 条件四元式， result 不用填写，if 自动生成 goto
     * @param op 比较符号
     * @param arg1 操作数1
     * @param arg2 操作数2
     * @return 返回需要回填的行号
     */
    public static int gen(String op,String arg1,String arg2){
        FourFormula.gen(op,arg1,arg2,"goto "+String.valueOf(FourFormula.getLine()+2));
        FourFormula.gen("goto",null,null,"?");
        return FourFormula.getLine()-1;
    }

    /**
     * 回填函数
     * @param before 需要回填 goto 语句的行号
     */
    public static void backPatch(int before){
        SymbolTable.fourFormulas.get(before).setResult(String.valueOf(FourFormula.getLine()));
    }

}
