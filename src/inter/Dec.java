package inter;

import LL1.Expression;
import common.MyException;
import common.Tag;
import common.Token;

import java.util.ArrayList;

/**
 * 声明语句识别
 * P -> T = C
 * T -> int | double
 * C -> Expression D
 * D -> ,C | ;
 */
public class Dec {

    private int type;
    private ArrayList<Token> arrayList;
    private Expression expression;

    public Dec(){
        expression = new Expression();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        if(type != Tag.INT && type != Tag.DOUBLE)
            throw new MyException(MyException.DECERROR,-1);
        this.type = type;
    }

    public ArrayList<Token> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Token> arrayList) {
        this.arrayList = arrayList;
    }

    public void gen(){

        expression.setList(arrayList);

    }

    public String eliminate(String name, Number num){
        System.out.println("(=,"+num+",_,"+name+")");
        return "(=,"+num+",_,"+name+")";
    }

}
