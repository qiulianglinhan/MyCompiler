package LL1;

import common.MyException;
import common.Tag;
import common.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * 表达式求值递归写法
 * https://blog.csdn.net/qq_36081539/article/details/79678090?utm_source=blogxgwz0
 *
 * expr = term | + | -
 * term = factor | * | /
 * factor = (expr) | num
 */
public class Expression {

    private List<Token> list;
    private int point ;

    public void setList(List<Token> list) {
        point = 0;
        this.list = list;
    }

    public double expressionValue(){
        double result = termValue();
        boolean hasMore = true;
        while (hasMore){
            if (point >= list.size())   // 表达式读取完毕
                break;
            Token token = list.get(point);
            if(token.getType() == Tag.PLUS || token.getType() == Tag.MINUS){
                point++;
                if (point >= list.size())
                    throw new MyException(MyException.EXPRESSIONERROR,-1);
                double value = termValue();
                if(token.getType() == Tag.PLUS)
                    result += value;
                else
                    result -= value;
            }else{
                hasMore = false;
            }
        }
        return result;
    }

    private double termValue(){
        double result = factorValue();
        while (true){
            if (point >= list.size())
                break;
            Token token = list.get(point);
            if(token.getType() == Tag.MULTI || token.getType() == Tag.DIV){
                point++;
                if (point >= list.size())
                    throw new MyException(MyException.EXPRESSIONERROR,-1);
                double value = factorValue();
                if(token.getType() == Tag.MULTI)
                    result *= value;
                else{
                    if(value == 0)
                        throw new MyException(MyException.DIVERROR,-1);
                    result /= value;
                }
            }else
                break;
        }
        return result;
    }

    private double factorValue(){
        double result = 0;
        Token token = list.get(point);
        if(token.getType() == Tag.LEFT_BRACKET){    // 左括号，引入新的表达式
            point++;    // 继续向后读
            if (point >= list.size())
                throw new MyException(MyException.EXPRESSIONERROR,-1);
            result = expressionValue();
            if (list.get(point).getType() != Tag.RIGHT_BRACKET)
                throw new MyException(MyException.BRACKETERROR,-1);
            point++;    // 读取右括号
            if (point >= list.size())
                throw new MyException(MyException.EXPRESSIONERROR,-1);
        }else if (token.getType() == Tag.NUMINT){  // 常数
            result = Integer.parseInt(token.getContent());
            point++;
        }else if(token.getType() == Tag.NUMDOUBLE){
            result = Double.parseDouble(token.getContent());
            point++;
        }else if(token.getType() == Tag.NUMFLOAT){
            result = Float.parseFloat(token.getContent());
            point++;
        }else{
            throw new MyException(-1,-1);
        }
        return result;
    }

    /*  // 主函数测试
    public static void main(String[] args) {
        ArrayList<Token> list = new ArrayList<>();
        // ((30+4)+(2+2.5)/(4+5))-3
        Token token1 = new Token(Tag.LEFT_BRACKET,"(");
        Token token2 = new Token(Tag.LEFT_BRACKET,"(");
        Token token3 = new Token(Tag.INT,"30");
        Token token4 = new Token(Tag.PLUS,"+");
        Token token5 = new Token(Tag.INT,"4");
        Token token6 = new Token(Tag.RIGHT_BRACKET,")");
        Token token7 = new Token(Tag.PLUS,"+");
        Token token8 = new Token(Tag.LEFT_BRACKET,"(");
        Token token9 = new Token(Tag.INT,"2");
        Token token20 = new Token(Tag.PLUS,"+");
        Token token10 = new Token(Tag.DOUBLE,"2.5");
        Token token21 = new Token(Tag.RIGHT_BRACKET,")");
        Token token11 = new Token(Tag.DIV,"/");
        Token token12 = new Token(Tag.LEFT_BRACKET,"(");
        Token token13 = new Token(Tag.INT,"4");
        Token token14 = new Token(Tag.PLUS,"+");
        Token token15 = new Token(Tag.INT,"5");
        Token token16 = new Token(Tag.RIGHT_BRACKET,")");
        Token token17 = new Token(Tag.RIGHT_BRACKET,")");
        Token token18 = new Token(Tag.MINUS,"-");
        //Token token19 = new Token(Tag.INT,"3");
        list.add(token1);list.add(token2);list.add(token3);list.add(token4);list.add(token5);
        list.add(token6);list.add(token7);list.add(token8);list.add(token9);list.add(token20);list.add(token10);
        list.add(token21);list.add(token11);list.add(token12);list.add(token13);list.add(token14);list.add(token15);
        list.add(token16);list.add(token17);list.add(token18);//list.add(token19);
        Expression expression = new Expression(list);
        System.out.println(expression.expressionValue());
    }
    */

}

/*
((30+4)+(2+2.5)/(4+5))-3

31.5
 */