package inter;

import common.SymbolTable;

public class Else {

    /**
     * else 语句产生式代码
     * @return 运行else语句时当前行号，用于回填使用
     */
    public static int gen(){
        FourFormula.gen("goto",null,null,"?");
        return FourFormula.getLine()-1;
    }

    /**
     * 回填函数
     * 注： 在有else部分时，if语句生成 goto 时跳转地址不正确，需要重新回填
     * @param ifBefore
     * @param before
     */
    public static void backPatch(int ifBefore,int before){
        SymbolTable.fourFormulas.get(ifBefore).setResult(String.valueOf(
                Integer.parseInt(SymbolTable.fourFormulas.get(ifBefore).getResult().toString())+1
        )); // 修改 if 的 goto 语句
        // 填写 else 部分的 goto 语句
        SymbolTable.fourFormulas.get(before).setResult(String.valueOf(FourFormula.getLine()));
    }
}
