import common.Tag;
import common.Token;
import inter.Assign;
import inter.Dec;
import inter.Inter;

import java.util.ArrayList;

public class TestAssign {
    public static void main(String[] args) {
        // declare begin
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
        // declare end

        // assign begin
        Token token16 = new Token(Tag.IDENTIFY,"num1");
        Token token17 = new Token(Tag.EQ,"=");
        Token token18 = new Token(Tag.NUMINT,"53");
        Token token19 = new Token(Tag.COMMA,",");
        Token token20 = new Token(Tag.IDENTIFY,"num4");
        Token token21 = new Token(Tag.EQ,"=");
        Token token22 = new Token(Tag.NUMINT,"34");
        Token token23 = new Token(Tag.PLUS,"+");
        Token token24 = new Token(Tag.NUMINT,"6");
        Token token25 = new Token(Tag.SEMICOLON,";");
        list.clear();
        list.add(token16);list.add(token17);list.add(token18);list.add(token19);
        list.add(token20);list.add(token21);list.add(token22);list.add(token23);
        list.add(token24);list.add(token25);
        Assign assign = new Assign(token16.getContent(),list);
        assign.gen();
        // assign end

    }
}
/*
(601,num2)
(306,=)
(401,53)
(506,;)
 */
