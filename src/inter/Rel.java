package inter;

import LL1.Expression;
import common.MyException;
import common.Token;

import java.util.ArrayList;

/**
 * 表达式比较
 */
public class Rel {

    private Expression leftExpression,rightExpression;
    private ArrayList<Token> leftList,rightList;
    private int op;

    /**
     * 含有 Rel 的表达式
     * @param totList 全部 Rel 表达式
     */
    public Rel(ArrayList<Token> totList){
        int idx = 0;
        leftList = new ArrayList<>();
        rightList = new ArrayList<>();
        while (!Inter.tagArrayList.contains(totList.get(idx).getType())){
            leftList.add(totList.get(idx++));
        }
        if (idx == totList.size())  // none rel, throw error
            throw new MyException(MyException.RELERROR,-1);
        op = totList.get(idx++).getType();
        for (; idx < totList.size(); idx++) {
            rightList.add(totList.get(idx));
        }
        leftExpression = new Expression(leftList);
        rightExpression = new Expression(rightList);
    }

    public void gen(){
        eliminate(op,String.valueOf(leftExpression.expressionValue()),String.valueOf(rightExpression.expressionValue()),"null");
    }

    public String eliminate(int opType,String arg1,String arg2,String result){
        String t1,t2;
        if (arg1 == null || arg1.equals(""))
            t1 = "_";
        else
            t1 = arg1;
        if (arg2 == null || arg2.equals(""))
            t2 = "_";
        else
            t2 = arg2;
        System.out.println("("+Inter.tag2low.get(opType)+","+t1+","+t2+","+result+")");
        return "("+Inter.tag2low.get(opType)+","+t1+","+t2+","+result+")";
    }
}
