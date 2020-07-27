package inter;

import common.SymbolTable;

import java.util.ArrayList;

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
     * @param ifBefore if语句下面的goto语句回填行号
     * @param before else部分的需要修改语句
     */
    public static void backPatch(int ifBefore,int before){
        SymbolTable.fourFormulas.get(ifBefore).setResult(String.valueOf(
                Integer.parseInt(SymbolTable.fourFormulas.get(ifBefore).getResult())+1
        )); // 修改 if 的 goto 语句
        // 填写 else 部分的 goto 语句
        SymbolTable.fourFormulas.get(before).setResult(String.valueOf(FourFormula.getLine()));
    }

    /**
     * 回填函数
     * 注： 在有else部分时，if语句生成 goto 时跳转地址不正确，需要重新回填
     * eg：
     *      code: if(a < b)
     *              x = 0;
     *            else
     *              x = 1;
     *      未修改：
     *                 0: [<,a,b,goto 2]
     *                 1: [goto,_,_,3]
     *                 2: [=,0,_,x]
     *                 3: [goto,_,_,?]
     *                 4: [=,1,_,x]
     *      修改后：
     *                 0: [<,a,b,goto 2]
     *                 1: [goto,_,_,4]      <-- 修改此部分
     *                 2: [=,0,_,x]
     *                 3: [goto,_,_,5]
     *                 4: [=,1,_,x]
     * @param ifGenLineList 需要修改回填的列表
     * @param before else部分的需要修改语句
     */
    public static void backPatch(ArrayList<Integer> ifGenLineList, int before){
        for (Integer ifBefore : ifGenLineList) {
            SymbolTable.fourFormulas.get(ifBefore).setResult(String.valueOf(
                    Integer.parseInt(SymbolTable.fourFormulas.get(ifBefore).getResult())+1
            )); // 修改 if 的下一行的 goto 语句
        }
        // 填写 else 部分的 goto 语句
        SymbolTable.fourFormulas.get(before).setResult(String.valueOf(FourFormula.getLine()));
    }
}
