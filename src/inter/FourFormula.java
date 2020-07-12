package inter;

/**
 * 四元式
 */
public class FourFormula {

    private final String op;      // 符号
    private final Object arg1;    // 参数一
    private final Object arg2;    // 参数二
    private final Object result;  // 结果

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
        System.out.println("["+op+","+arg1+","+arg2+","+result+"]");
    }

}
