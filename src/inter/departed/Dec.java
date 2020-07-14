package inter.departed;

import LL1.departed.Expression;
import common.*;

import java.util.ArrayList;

/**
 * 声明语句识别
 * S -> PT
 * P -> int | double
 * T -> id D | id = Expression D
 * D -> ,T | ;
 */
@Deprecated
public class Dec {

    private int type;                       // 声明语句类型
    private ArrayList<Token> arrayList;     // 声明语句序列
    private Expression expression;          // 表达式求值实例
    private ArrayList<Token> subArrayList;  // 表达式语句序列

    private int peek = 1;                   // Token序列顶部指针，0是声明类型

    public Dec(){
        expression = new Expression();
        subArrayList = new ArrayList<>();
    }

    public Dec(int type,ArrayList<Token> arrayList){
        if(type != Tag.INT && type != Tag.DOUBLE)
            throw new MyException(MyException.DECERROR,-1);
        if (arrayList.size() < 3){
            System.err.println("声明语句不满足产生式规则");
            System.exit(Exit.DECERROR);
        }
        this.type = type;
        this.arrayList = arrayList;
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
        if (arrayList.size() < 3){
            System.err.println("声明语句不满足产生式规则");
            System.exit(Exit.DECERROR);
        }
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

    /**
     * 解析并生成三地址码
     */
    public void gen(){
        if (arrayList.get(peek).getType() != Tag.IDENTIFY)
            throw new MyException(MyException.IDENTIFYERROR,-1);
        String name = arrayList.get(peek++).getContent();

        if (arrayList.get(peek).getType() == Tag.ASSIGN){
            peek++; // jump eq =
            getExpressionList();
            expression.setList(subArrayList);
            double dtmp = expression.expressionValue();
            int itmp = (int) dtmp;
            if (this.type == Tag.INT){
                Num num = new Num(Tag.NUMINT,String.valueOf(itmp),itmp,true);
                Inter.declaration.put(name,num);
                eliminate(name,itmp);
            }else{
                Real real = new Real(Tag.NUMDOUBLE,String.valueOf(dtmp),dtmp,true);
                Inter.declaration.put(name,real);
                eliminate(name,dtmp);
            }
            if (arrayList.get(peek).getType() == Tag.COMMA){
                peek++; // jump ,
                gen();
            }
            if (arrayList.get(peek).getType() == Tag.SEMICOLON) // ;
                return;
        }else if (arrayList.get(peek).getType() == Tag.SEMICOLON || arrayList.get(peek).getType() == Tag.COMMA){ // ;
            if (this.type == Tag.INT){
                Num num = new Num(Tag.NUMINT,"0",0,false);
                Inter.declaration.put(name,num);
                eliminate(name,0);
            }else{
                Real real = new Real(Tag.NUMDOUBLE,"0",0,false);
                Inter.declaration.put(name,real);
                eliminate(name,0);
            }
            if (arrayList.get(peek).getType() == Tag.COMMA){
                peek++; // jump ,
                gen();
            }
        } else
            throw new MyException(MyException.DECERROR,-1);

    }

    /**
     * 生成三地址码
     * @param name 生成identify名称
     * @param num  生成数字
     * @return  三地址码字符串表示
     */
    public String eliminate(String name, Number num){
        System.out.println("(=,"+num+",_,"+name+")");
        return "(=,"+num+",_,"+name+")";
    }

}
