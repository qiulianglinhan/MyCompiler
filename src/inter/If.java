package inter;

import common.SymbolTable;

import java.util.ArrayList;

public class If {

    /**
     * 生成 if 条件四元式， result 不用填写，if 自动生成 goto
     * @param op 比较符号
     * @param arg1 操作数1
     * @param arg2 操作数2
     * @return 返回需要回填的行号。注意，此行号意思为：
     *              eg: 行 号        四元式
     *                   12  : [<,a,b,goto 14]
     *                   13  : [goto,_,_,?]     <- 返回此行号
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

    /**
     * 回填函数
     * @param ifGenLineList 需要回填 goto 语句的行号列表，用于if语句中多重判断（&& ||）
     */
    public static void backPatch(ArrayList<Integer> ifGenLineList){
        for (Integer before : ifGenLineList) {
            SymbolTable.fourFormulas.get(before).setResult(String.valueOf(FourFormula.getLine()));
        }
    }

    /**
     * 修改if语句最后的 goto 部分 eg: [<,a,b,goto 2]只修改2部分
     * 此修改一般用于 or（||） 部分的修改
     * @param ifLine if语句的第一行。eg:
     *               1: [<,a,b,goto 2]        <- 修改此部分，下一行不修改
     *               2: [goto,_,_,10]
     */
    public static void backPatchIfStat(int ifLine){
        SymbolTable.fourFormulas.get(ifLine).setResult("goto "+FourFormula.getLine());
    }

}
