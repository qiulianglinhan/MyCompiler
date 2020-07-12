import common.Tag;
import common.Token;
import inter.sub.Dec;
import inter.sub.Inter;

import java.util.ArrayList;

public class TestDec {

    public static void main(String[] args) {
        // declaration: int num1 = 2+3*2,num2,num3 = 56;
        Inter inter = new Inter();
        Token token1 = new Token(Tag.INT,"int");
        Token token2 = new Token(Tag.IDENTIFY,"num1");
        Token token3 = new Token(Tag.EQ,"=");
        Token token4 = new Token(Tag.NUMINT,"2");
        Token token5 = new Token(Tag.PLUS,"+");
        Token token6 = new Token(Tag.NUMINT,"3");
        Token token7 = new Token(Tag.MULTI,"*");
        Token token8 = new Token(Tag.NUMINT,"2");
        Token token9 = new Token(Tag.COMMA,",");
        Token token10 = new Token(Tag.IDENTIFY,"num2");
        Token token11 = new Token(Tag.COMMA,",");
        Token token12 = new Token(Tag.IDENTIFY,"num3");
        Token token13 = new Token(Tag.EQ,"=");
        Token token14 = new Token(Tag.NUMINT,"56");
        Token token15 = new Token(Tag.SEMICOLON,";");
        ArrayList<Token> list = new ArrayList<>();
        list.add(token1);list.add(token2);list.add(token3);list.add(token4);list.add(token5);
        list.add(token6);list.add(token7);list.add(token8);
        list.add(token9);list.add(token10);list.add(token11);
        list.add(token12);list.add(token13);list.add(token14);list.add(token15);
        Dec dec = new Dec();
        dec.setType(token1.getType());
        dec.setArrayList(list);
        dec.gen();
    }

}
/*
(401,int)
(601,num1)
(306,=)
(401,2)
(301,+)
(401,3)
(303,*)
(401,2)
(505,,)
(601,num2)
(505,,)
(601,num3)
(306,=)
(401,56)
(506,;)
 */