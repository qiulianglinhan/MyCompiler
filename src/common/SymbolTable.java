package common;

import inter.FourFormula;

import java.util.*;

public class SymbolTable {
    public static Set<String> KEYWORDS = new HashSet<>();       // 保留关键字
    public static Set<Integer> COMPAREWORDS = new HashSet<>();   // 比较关键字
    public static Map<String,Integer> SYMBOL2TAG = new HashMap<>();
    public static Map<Integer,String> TAG2SYMBOL = new HashMap<>();
    public static Stack<Token> TOEKNS = new Stack<>();  // 存放 Token 对象
    public static Map<String,Token> SYMBOLES = new HashMap<>();
    public static int tmpVariable = 0;
    public static ArrayList<FourFormula> fourFormulas = new ArrayList<>();  // 存放四元式序列

    static {
        // keywords begin
        KEYWORDS.add("int");KEYWORDS.add("double");KEYWORDS.add("main");KEYWORDS.add("if");
        KEYWORDS.add("else");KEYWORDS.add("for");KEYWORDS.add("while");KEYWORDS.add("return");
        KEYWORDS.add("void");KEYWORDS.add("break");KEYWORDS.add("continue");KEYWORDS.add("do");
        // keywords end

        // symbol to tag begin
        // keywords begin
        SYMBOL2TAG.put("if",Tag.IF);SYMBOL2TAG.put("else",Tag.ELSE);SYMBOL2TAG.put("break",Tag.BREAK);
        SYMBOL2TAG.put("continue",Tag.CONTINUE);SYMBOL2TAG.put("do",Tag.DO);SYMBOL2TAG.put("while",Tag.WHILE);
        SYMBOL2TAG.put("for",Tag.FOR);SYMBOL2TAG.put("void",Tag.VOID);SYMBOL2TAG.put("main",Tag.MAIN);
        SYMBOL2TAG.put("int",Tag.INT);SYMBOL2TAG.put("double",Tag.DOUBLE);SYMBOL2TAG.put("return",Tag.RETURN);
        // keywords end

        // sign begin
        SYMBOL2TAG.put("+",Tag.PLUS);SYMBOL2TAG.put("-",Tag.MINUS);SYMBOL2TAG.put("*",Tag.MULTI);
        SYMBOL2TAG.put("/",Tag.DIV);SYMBOL2TAG.put("%",Tag.MOD);SYMBOL2TAG.put("=",Tag.ASSIGN);
        SYMBOL2TAG.put(">",Tag.GT);SYMBOL2TAG.put("<",Tag.LW);SYMBOL2TAG.put(">=",Tag.GE);
        SYMBOL2TAG.put("<=",Tag.LE);SYMBOL2TAG.put("==",Tag.EQ);SYMBOL2TAG.put("!=",Tag.NE);
        SYMBOL2TAG.put("++",Tag.PLUSPLUS);SYMBOL2TAG.put("--",Tag.MINUSMINUS);SYMBOL2TAG.put("&&",Tag.AND);
        SYMBOL2TAG.put("||",Tag.OR);SYMBOL2TAG.put("&",Tag.BITAND);SYMBOL2TAG.put("|",Tag.BITOR);
        SYMBOL2TAG.put("!",Tag.NOT);SYMBOL2TAG.put("&=",Tag.ANDEQ);SYMBOL2TAG.put("|=",Tag.OREQ);
        SYMBOL2TAG.put("<<",Tag.LEFTSHIFT);SYMBOL2TAG.put(">>",Tag.RIGHTSHIFT);
        SYMBOL2TAG.put("+=",Tag.PLUSEQ);SYMBOL2TAG.put("-=",Tag.MINUSEQ);
        SYMBOL2TAG.put("*=",Tag.MULTIEQ);SYMBOL2TAG.put("/=",Tag.DIVEQ);
        // sign end

        // sign begin
        SYMBOL2TAG.put("(",Tag.LEFT_BRACKET);SYMBOL2TAG.put(")",Tag.RIGHT_BRACKET);
        SYMBOL2TAG.put("{",Tag.LEFT_FBRACKET);SYMBOL2TAG.put("}",Tag.RIGHT_FBRACKET);
        SYMBOL2TAG.put(",",Tag.COMMA);SYMBOL2TAG.put(";",Tag.SEMICOLON);
        SYMBOL2TAG.put("\"",Tag.DOUBLE_QUOTATION);SYMBOL2TAG.put("'",Tag.QUOTATION);
        // sign end
        // symbol to tag end
        
        // tag to symbol begin
        SYMBOL2TAG.forEach((k,v)-> TAG2SYMBOL.put(v,k));
        // tag to symbol end

        // compare words begin
        COMPAREWORDS.add(Tag.EQ);COMPAREWORDS.add(Tag.GT);COMPAREWORDS.add(Tag.LW);
        COMPAREWORDS.add(Tag.GE);COMPAREWORDS.add(Tag.LE);COMPAREWORDS.add(Tag.NE);
        // compare words end

    }

    public static String getNewTmpVariable(){
        return "$"+tmpVariable++;
    }

}
