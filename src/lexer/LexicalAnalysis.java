package lexer;

import common.MyException;
import common.SymbolTable;
import common.Tag;
import common.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LexicalAnalysis {
    private LexScanner lexScanner;
    private String description;
    private String token;

    private char pch;
    private int categoryCode = 0;
    private int line = 1;

    private ArrayList<Token> tokens = new ArrayList<>();    // 存放词法单元，此处为了避免不再从文件读取

    /**
     * 词法分析
     * @param filename 待词法分析的.c源文件
     * @throws IOException 词法分析错误
     */
    public LexicalAnalysis(String filename) throws IOException {
        lexScanner = new LexScanner(filename);
        this.lexicalAnalysisProcess();
    }

    public String getToken() {
        return token;
    }

    private void lexicalAnalysisProcess() throws IOException {
        while (true){
            pch = lexScanner.next();
            if(pch == (char)-1) // 结束返回-1
                break;

            switch (pch){
                // 分界符
                case '(': case ')': case '{': case '}': case ',': case ';':
                    description = "单字符分界符";
                    token = ""+pch;
                    break;

                // + 所有操作
                case '+':
                    pch = lexScanner.next();
                    switch (pch){
                        case '+':
                            description = "加加";
                            token = "++";
                            break;
                        case '=':
                            description = "加等于";
                            token = "+=";
                            break;
                        default:
                            description = "加法";
                            token = "+";
                            lexScanner.back(pch);
                            break;
                    }
                    break;

                // - 所有操作
                case '-':
                    pch = lexScanner.next();
                    switch (pch){
                        case '-':
                            description = "减减";
                            token = "--";
                            break;
                        case '=':
                            description = "减等于";
                            token = "-=";
                            break;
                        default:
                            description = "减法";
                            token = "-";
                            lexScanner.back(pch);
                            break;
                    }
                    break;

                // * 所有操作
                case '*':
                    pch = lexScanner.next();
                    switch (pch){
                        case '/':
                            description = "右注释";
                            token = "*/";
                            break;
                        case '=':
                            description = "乘等于";
                            token = "*=";
                            break;
                        default:
                            description = "乘法";
                            token = "*";
                            lexScanner.back(pch);
                            break;
                    }
                    break;

                // / 所有操作
                case '/':
                    pch = lexScanner.next();
                    switch (pch){
                        case '=':
                            description = "除等于";
                            token = "/=";
                            break;
                        case '/':   // 单行注释
                            description = "单行注释";
                            token = "//";
                            while(true)
                            {
                                // Windows 换行 \r\n
                                pch = lexScanner.next();
                                if(pch=='\r')
                                {
                                    //读到\r表示开始读回车符，读取下一个\n;
                                    lexScanner.next();
                                    line++;
                                    break;
                                }
                            }
                            break;
                        case '*':
                            description = "左注释";
                            token = "/*";
                            boolean hasEnd = false;
                            while (true){
                                pch = lexScanner.next();
                                if(pch == 65535)    // 读取到文件尾部也没有匹配到注释结束符号
                                    break;
                                if(pch=='*')
                                {
                                    pch = lexScanner.next();
                                    if(pch=='/')
                                    {
                                        char buf[]=new char[2];
                                        buf[0]='/';
                                        buf[1]='*';
                                        lexScanner.backChars(buf); // 退回右注释
                                        hasEnd = true;
                                        break;
                                    }
                                }
                            }
                            if(!hasEnd)
                                throw new MyException(MyException.COMMENTERROR,line);
                            break;
                        default:
                            description = "除法";
                            token = "/";
                            lexScanner.back(pch);
                            break;
                    }
                    break;

                // & 所有操作
                case '&':
                    pch = lexScanner.next();
                    switch(pch) {
                        case '=':
                            description = "与等于";
                            token = "&=";
                            break;
                        case '&':
                            description = "与";
                            token = "&&";
                            break;
                        default:
                            description = "位与";
                            token = "&";
                            lexScanner.back(pch);
                            break;
                    }
                    break;

                // | 所有操作
                case '|':
                    pch = lexScanner.next();
                    switch(pch) {
                        case '=':
                            description = "或等于";
                            token = "|=";
                            break;
                        case '|':
                            description = "或";
                            token = "||";
                            break;
                        default:
                            description = "位或";
                            token = "|";
                            lexScanner.back(pch);
                            break;
                    }
                    break;

                // = 所有操作
                case '=':
                    pch = lexScanner.next();
                    if (pch == '=') {
                        description = "等于";
                        token = "==";
                    } else {
                        description = "赋值";
                        token = "=";
                        lexScanner.back(pch);
                    }
                    break;
                // ! 所有操作
                case '!':
                    pch = lexScanner.next();
                    if (pch == '=') {
                        description = "不等于";
                        token = "!=";
                    } else {
                        description = "取反";
                        token = "!";
                        lexScanner.back(pch);
                    }
                    break;

                // < 所有操作
                case '<':
                    pch = lexScanner.next();
                    switch(pch) {
                        case '=':
                            description = "小于等于";
                            token = "<=";
                            break;
                        case '<':
                            description = "左移";
                            token = "<<";
                            break;
                        default:
                            description = "小于";
                            token = "<";
                            lexScanner.back(pch);
                    }
                    break;

                // > 所有操作
                case '>':
                    pch = lexScanner.next();
                    switch(pch) {
                        case '=':
                            description = "大于等于";
                            token = ">=";
                            break;
                        case '>':
                            description = "右移";
                            token = ">>";
                            break;
                        default:
                            description = "大于";
                            token = ">";
                            lexScanner.back(pch);
                    }
                    break;

                // 空格
                case ' ':case '\t':
                    // 空格和制表符忽略
                    token = "";
                    break;

                // [
                case '[':
                    token = "[";
                    break;

                // ]
                case ']':
                    token = "]";
                    break;

                case ':':
                    token = ":";
                    break;

                case '\r':
                    // 读到\r表示开始读回车符，读取下一个\n;（Windows系统）
                    lexScanner.next();
                    token = "";
                    line++;
                    break;

                // 变量或者数字
                default:
                    // 此部分识别变量和数字
                    token = "";
                    description = "";
                    if (Character.isDigit(pch)) {   // handler number (support int or double temporarily)
                        token = ""+pch;
                        description = "int";
                        while (Character.isDigit(pch) || pch == '.') {
                            pch = lexScanner.next();
                            if (Character.isDigit(pch))
                                token += pch;
                            else if (pch == '.') {  // ready to handler double number
                                token += pch;
                                pch = lexScanner.next();
                                if (Character.isDigit(pch)) {
                                    while (Character.isDigit(pch)) {
                                        token += pch;
                                        pch = lexScanner.next();
                                    }
                                    if (Character.isLetter(pch) || pch == '.')
                                        throw new MyException(MyException.NUMERROR,line);
                                    description = "double";
                                    break;
                                }else
                                    throw new MyException(MyException.NUMERROR,line);
                            }
                        }
                        lexScanner.back(pch);
                    }else if (Character.isLetter(pch)) {    // handler identification
                        token = ""+pch;
                        pch = lexScanner.next();
                        while (Character.isLetterOrDigit(pch)) {
                            token += pch;
                            pch = lexScanner.next();
                        }
                        description = "id";
                        lexScanner.back(pch);
                    }else
                        throw new MyException(MyException.UNKNOWWORD,line);
            }

            // skip space, line comment, block comment.
            // Those signs needn't be written to lex file
            if (token.equals("") || token.equals("//") || token.equals("/*") || token.equals("*/")){
                token = "";
                description = "";
                continue;
            }


            //查表，输出
            if (description.equals("int"))
                categoryCode = Tag.NUMINT;
            else if (description.equals("double"))
                categoryCode = Tag.NUMDOUBLE;
            else if (SymbolTable.SYMBOL2TAG.get(token) != null)
                categoryCode = SymbolTable.SYMBOL2TAG.get(token);
            else
                categoryCode = Tag.IDENTIFY;
            outMidFile();

            // add to tokens arraylist
            tokens.add(new Token(categoryCode,token,line));

            // reinitialize
            description = "";
            token = "";

        }

        // 最后手动关闭流
        if (lexScanner.closeStream())
            System.out.println("词法分析成功，生成词法文件为"+lexScanner.getOutMidFileName());

        // put stack
        Collections.reverse(tokens);
        SymbolTable.TOKENS.addAll(tokens);

    }

    //输出二元式,向中间文件写入二元式
    private void outMidFile() throws IOException{
        System.out.println("["+categoryCode+","+token+"]");
        lexScanner.writeToFile(categoryCode,token,line);
    }

}
