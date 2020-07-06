package inter;

import LL1.Expression;
import common.*;

import java.util.ArrayList;

/**
 * 赋值语句
 * S -> id = Expression
 */
public class Assign {
    private int type;                       // id类型
    private ArrayList<Token> arrayList;     // 赋值语句序列
    private Expression expression;          // 表达式求值实例
    private ArrayList<Token> subArrayList;  // 表达式语句序列

    private int peek = 0;

    public Assign(String idName,ArrayList<Token> arrayList){
        Token token = Inter.declaration.get(idName);
        // TODO: line is -1 temporarily
        if (token == null){
            System.err.println("标识符"+idName+"未找到");
            throw new MyException(MyException.IDENTIFYNOTFOUND,-1);
        }

        subArrayList = new ArrayList<>();
        expression = new Expression();
        this.arrayList = arrayList;
        this.type = token.getType();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Token> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Token> arrayList) {
        if (arrayList.size() <= 3)
            throw new MyException(MyException.DECERROR,-1);
        this.arrayList = arrayList;
    }

    private void getExpressionList(){
        subArrayList.clear();
        while (true){
            if (arrayList.get(peek).getType() == Tag.SEMICOLON)
                break;
            if (arrayList.get(peek).getType() == Tag.COMMA)
                break;
            subArrayList.add(arrayList.get(peek++));
        }
    }

    public void gen(){
        if (arrayList.get(peek).getType() != Tag.IDENTIFY)
            throw new MyException(MyException.IDENTIFYERROR,-1);
        String name = arrayList.get(peek++).getContent();
        if (Inter.declaration.get(name) == null){
            System.err.println("标识符"+name+"未找到");
            throw new MyException(MyException.IDENTIFYNOTFOUND,-1);
        }

        if (arrayList.get(peek).getType() == Tag.EQ){
            peek++; // jump eq =
            getExpressionList();
            expression.setList(subArrayList);
            double dtmp = expression.expressionValue();
            int itmp = (int) dtmp;
            if (this.type == Tag.NUMINT){
                Num num = (Num) Inter.declaration.get(name);
                num.setInit(true);
                num.setValue(itmp);
                Inter.declaration.put(name,num);    // map 自动替换已有项
                eliminate(Tag.EQ,String.valueOf(itmp),null,name);
            }else{
                Real real = (Real) Inter.declaration.get(name);
                real.setInit(true);
                real.setValue(dtmp);
                Inter.declaration.put(name,real);
                eliminate(Tag.EQ,String.valueOf(dtmp),null,name);
            }
            if (arrayList.get(peek).getType() == Tag.COMMA){
                peek++; // jump ,
                gen();
            }
            if (arrayList.get(peek).getType() == Tag.SEMICOLON) // ;
                return;
        } else
            throw new MyException(MyException.DECERROR,-1);
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
        // TODO: opType uses "=" temporarily while map completed
        System.out.println("(=,"+t1+","+t2+","+result+")");
        return "(=,"+t1+","+t2+","+result+")";
    }
}
