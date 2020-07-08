package common;

/**
 * 基本Tag，不够再回来填写
 */

public class Tag {
    public static final int
    IF = 201, ELSE = 202, BREAK = 203, CONTINUE = 204, DO = 205, WHILE = 206, FOR = 207, TRUE = 208,
            FALSE = 209, VOID = 210, MAIN = 211, INT = 212, DOUBLE = 213,RETURN=214;

    public static final int
    PLUS = 301, MINUS = 302, MULTI = 303, DIV = 304, MOD = 305, ASSIGN = 306,
    GT = 307, LW = 308, GE = 309, LE = 310, EQ = 311, NE = 312, PLUSPLUS = 313,
    MINUSMINUS = 314, AND = 315, OR = 316, BITAND = 317, BITOR = 318, NOT = 319,
    ANDEQ = 320, OREQ = 321, LEFTSHIFT = 322, RIGHTSHIFT = 323, PLUSEQ = 324,
    MINUSEQ = 325, MULTIEQ = 326, DIVEQ = 327;

    public static final int
    NUMINT = 401, NUMDOUBLE = 402;

    public static final int
    LEFT_BRACKET = 501, RIGHT_BRACKET = 502, LEFT_FBRACKET = 503, RIGHT_FBRACKET = 504,
    COMMA = 505, SEMICOLON = 506, DOUBLE_QUOTATION = 507, QUOTATION = 508;

    public static final int
    IDENTIFY = 601;

}
/*
关键字：
if else break continue do while for true false void main int double

运算符号：
+ - * / % = > < >= <= == != ++ -- && ||

数字符号：
int double

符号：
( ) { } , ; " '
 */