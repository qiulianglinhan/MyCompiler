package inter;

import java.util.ArrayList;

/**
 * 用于函数
 */
public class Function {
    private ArrayList<Parameter> params;
    private final int startLineNum;
    private int endLineNum;

    public Function(int startLineNum, int[] types, String[] ids){
        this.startLineNum = startLineNum;
        params = new ArrayList<>();
        assert types.length == ids.length;  // must equals
        for (int i = 0; i < types.length; i++) {
            params.add(new Parameter(types[i],ids[i]));
        }
    }

    public Function(int startLineNum){
        this.startLineNum = startLineNum;
        params = new ArrayList<>();
    }

    public ArrayList<Parameter> getParams() {
        return params;
    }

    public int getStartLineNum() {
        return startLineNum;
    }

    public int getEndLineNum() {
        return endLineNum;
    }

    public void setEndLineNum(int endLineNum) {
        this.endLineNum = endLineNum;
    }

    /**
     * 生成return语句四元式
     * @param result 生成四元式的结果
     * @return 生成return的四元式序号
     */
    public static int genReturnCode(String result){
        FourFormula.gen("return",null,null,result);
        return FourFormula.getLine()-1;
    }

    /**
     * 生成call function 四元式
     * @param functionName 方法名称
     * @param result 存放方法返回的结果
     */
    public static void genCallFunctionCode(String functionName,String result){
        assert functionName != null;
        if (result == null)
            result = "_";
        FourFormula.gen("call",functionName,null,result);
    }


    public static void genParamCode(String arg,String result){

    }
}
