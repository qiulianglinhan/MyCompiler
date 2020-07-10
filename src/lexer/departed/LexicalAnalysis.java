package lexer.departed;

import common.MyException;

import java.io.IOException;
import java.io.PushbackReader;

@Deprecated
public class LexicalAnalysis {
    LexScanner lexScanner = null;
    private String description;
    private String token;
    private PushbackReader pr = null;
    private final int STATEEND = 999;
    private SymbolTable symbolTable;

    private int state = 0;  // 状态转换图当前状态
    private char pch;
    private boolean midFileCreateSuccess = true;
    private int categoryCode = 0;
    private String value = "";
    private int line = 1;


    LexicalAnalysis(){
        lexScanner = new LexScanner("E:\\ideaPro\\MyCompiler\\src\\lexer\\test1.c");
        pr = lexScanner.getPr();
        symbolTable = new SymbolTable();
    }

    public String getDescription() {
        return description;
    }

    public String getToken() {
        return token;
    }

    public void lexicalAnalysisProcess(){
        if(pr == null){
            System.err.println("PushbackReader 错误！");
            System.exit(-1);
        }
        boolean keepScan = true;
        while (true){
            try {
                pch = (char) pr.read();
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
                        pch = (char) pr.read();
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
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // - 所有操作
                    case '-':
                        pch = (char) pr.read();
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
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // * 所有操作
                    case '*':
                        pch = (char) pr.read();
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
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // / 所有操作
                    case '/':
                        pch = (char) pr.read();
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
                                    pch = (char)pr.read();
                                    if(pch=='\r')
                                    {
                                        //读到\r表示开始读回车符，读取下一个\n;
                                        pr.read();
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
                                    pch = (char)pr.read();
                                    if(pch == 65535)    // 读取到文件尾部也没有匹配到注释结束符号
                                        break;
                                    if(pch=='*')
                                    {
                                        pch = (char)pr.read();
                                        if(pch=='/')
                                        {
                                            char buf[]=new char[2];
                                            buf[0]='*';
                                            buf[1]='/';
                                            pr.unread(buf); // 退回右注释
                                            hasEnd = true;
                                            break;
                                        }
                                    }
                                }
                                if(!hasEnd){
                                    throw new MyException(MyException.COMMENTERROR,line);
                                }
                                break;
                            default:
                                description = "除法";
                                token = "/";
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // & 所有操作
                    case '&':
                        pch = (char)pr.read();
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
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // | 所有操作
                    case '|':
                        pch = (char)pr.read();
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
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // = 所有操作
                    case '=':
                        pch = (char)pr.read();
                        switch(pch) {
                            case '=':
                                description = "等于";
                                token = "==";
                                break;
                            default:
                                description = "赋值";
                                token = "=";
                                pr.unread(pch);
                                break;

                        }
                        break;
                    // ! 所有操作
                    case '!':
                        pch = (char)pr.read();
                        switch(pch) {
                            case '=':
                                description = "不等于";
                                token = "!=";
                                break;
                            default:
                                description = "取反";
                                token = "!";
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // < 所有操作
                    case '<':
                        pch = (char)pr.read();
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
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // > 所有操作
                    case '>':
                        pch = (char)pr.read();
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
                                pr.unread(pch);
                                break;
                        }
                        break;

                    // 空格
                    case ' ':case '\t':
                        // 空格和制表符忽略
                        token = "";
                        break;
                    case '\r':
                        // 读到\r表示开始读回车符，读取下一个\n;（Windows系统）
                        pr.read();
                        token = "";
                        line++;
                        break;

                    // 变量或者数字
                    default:
                        // 此部分识别变量和数字
                        token = "";
                        description = "";
                        keepScan = true;
                        state = 0;
                        while(keepScan){
                            switch (state){
                                case 0:
                                    if(!Character.isLetterOrDigit(pch)){
                                        // 不满足变量名称、保留字和数字要求
                                        keepScan = false;
                                        break;
                                    }else if(pch == 'd'){
                                        state = 16;
                                    }else if(pch == 'i'){
                                        state = 5;
                                    }else if(pch == 'f'){
                                        state = 9;
                                    }else if(pch == 'v'){
                                        state = 1;
                                    }else if(pch == 'w'){
                                        state = 26;
                                    }else if(pch == 'm'){
                                        state = 31;
                                    }else if(pch == 'e'){
                                        state = 22;
                                    }else if(Character.isDigit(pch))
                                        state = 35;
                                    else if(pch == 'r')
                                        state = 36;
                                    else
                                        state = STATEEND;
                                    break;

                                case 1:
                                    if(pch == 'o')
                                        state = 2;
                                    else
                                        pch = back(pch);
                                    break;

                                case 2:
                                    if(pch == 'i')
                                        state = 3;
                                    else
                                        pch = back(pch);
                                    break;

                                case 3:
                                    if(pch == 'd')
                                        state = 4;
                                    else
                                        pch = back(pch);
                                    break;

                                case 4: case 6: case 8: case 13: case 15: case 21: case 25:
                                case 30: case 34: case 41:
                                    // 识别下一个字符是否为数字或者字母
                                    keepScan = toEndState(pch);
                                    break;

                                case 5:
                                    if(pch == 'f')
                                        state = 6;
                                    else if(pch == 'n')
                                        state = 7;
                                    else
                                        pch = back(pch);
                                    break;

                                case 7:
                                    if(pch == 't')
                                        state = 8;
                                    else
                                        pch = back(pch);
                                    break;

                                case 9:
                                    if(pch == 'l')
                                        state = 10;
                                    else if(pch == 'o')
                                        state = 14;
                                    else
                                        pch = back(pch);
                                    break;

                                case 10:
                                    if (pch == 'o')
                                        state = 11;
                                    else
                                        pch = back(pch);
                                    break;

                                case 11:
                                    if(pch == 'a')
                                        state = 12;
                                    else
                                        pch = back(pch);

                                case 12:
                                    if(pch == 't')
                                        state = 13;
                                    else
                                        pch = back(pch);
                                    break;

                                case 14:
                                    if (pch == 'r')
                                        state = 15;
                                    else
                                        pch = back(pch);
                                    break;

                                case 16:
                                    if (pch == 'o')
                                        state = 17;
                                    else
                                        pch = back(pch);
                                    break;

                                case 17:
                                    if(!Character.isLetterOrDigit(pch))
                                        keepScan = toEndState(pch);
                                    else if(pch == 'u')
                                        state = 18;
                                    else
                                        pch = back(pch);
                                    break;

                                case 18:
                                    if(pch == 'b')
                                        state = 19;
                                    else
                                        pch = back(pch);
                                    break;

                                case 19:
                                    if (pch == 'l')
                                        state = 20;
                                    else
                                        pch = back(pch);
                                    break;

                                case 20:
                                    if (pch == 'e')
                                        state = 21;
                                    else
                                        pch = back(pch);
                                    break;

                                case 22:
                                    if (pch == 'l')
                                        state = 23;
                                    else
                                        pch = back(pch);
                                    break;

                                case 23:
                                    if (pch == 's')
                                        state = 24;
                                    else
                                        pch = back(pch);
                                    break;

                                case 24:
                                    if (pch == 'e')
                                        state = 25;
                                    else
                                        pch = back(pch);
                                    break;

                                case 26:
                                    if (pch == 'h')
                                        state = 27;
                                    else
                                        pch = back(pch);
                                    break;

                                case 27:
                                    if (pch == 'i')
                                        state = 28;
                                    else
                                        pch = back(pch);
                                    break;

                                case 28:
                                    if (pch == 'l')
                                        state = 29;
                                    else
                                        pch = back(pch);
                                    break;

                                case 29:
                                    if (pch == 'e')
                                        state = 30;
                                    else
                                        pch = back(pch);
                                    break;

                                case 31:
                                    if (pch == 'a')
                                        state = 32;
                                    else
                                        pch = back(pch);
                                    break;

                                case 32:
                                    if (pch == 'i')
                                        state = 33;
                                    else
                                        pch = back(pch);
                                    break;

                                case 33:
                                    if (pch == 'n')
                                        state = 34;
                                    else
                                        pch = back(pch);
                                    break;

                                case 35:
                                    description = "无符号整数";
                                    if(Character.isDigit(pch)){
                                        state = 35;
                                    }else if(Character.isLetter(pch)){
                                        description = "";
                                        System.err.println("line:"+line+"出错。");
                                        throw new MyException(MyException.NUMERROR,line);
                                    }else if(pch == '.'){
                                        token += pch;
                                        pch = (char) pr.read();
                                        if(Character.isLetter(pch))
                                            throw new MyException(MyException.NUMERROR,line);
                                        while (Character.isDigit(pch)){
                                            token += pch;
                                            pch = (char) pr.read();
                                        }
                                        if(Character.isLetter(pch) || pch == '.')
                                            throw new MyException(MyException.NUMERROR,line);
                                        description = "无符号小数";
                                        keepScan = toEndState(pch);
                                    }else{
                                        keepScan = toEndState(pch);
                                    }
                                    break;

                                case 36:
                                    if (pch == 'e')
                                        state = 37;
                                    else
                                        pch = back(pch);
                                    break;

                                case 37:
                                    if (pch == 't')
                                        state = 38;
                                    else
                                        pch = back(pch);
                                    break;

                                case 38:
                                    if (pch == 'u')
                                        state = 39;
                                    else
                                        pch = back(pch);
                                    break;

                                case 39:
                                    if (pch == 'r')
                                        state = 40;
                                    else
                                        pch = back(pch);
                                    break;

                                case 40:
                                    if (pch == 'n')
                                        state = 41;
                                    else
                                        pch = back(pch);
                                    break;

                                default:
                                    if(Character.isLetterOrDigit(pch))
                                        state = STATEEND;
                                    else{
                                        keepScan = toEndState(pch);
                                        description = "标识符";
                                    }
                                    break;
                            }

                            if(pch != ' ')
                                token += pch;
                            // 继续从缓冲区读取字符
                            if(keepScan)
                                pch = (char) pr.read();
                        }

                        break;
                }
            } catch (IOException e) {
                System.err.println("PushBackReader 读取错误！");
                e.printStackTrace();
            }

            if(token.equals(""))
                continue;

            //查表，输出
            categoryCode = lookupSymbolTableList();
            outMidFile();
            value = null;
            description = "";
            token = "";
        }

        lexScanner.closeStream();
        if(!midFileCreateSuccess){
            System.err.println("词法分析出错！");
            System.exit(-2);
        }

    }

    private char back(char pch) throws IOException {
        state = STATEEND;
        pr.unread(pch);
        return ' ';
    }

    private boolean toEndState(char pch) throws IOException {
        if(!Character.isLetterOrDigit(pch)) {
            this.pch = this.back(pch);
            return false;
        }
        else
            return true;
    }

    //输出二元式,向中间文件写入二元式
    private void outMidFile() {
        if(categoryCode==0) {
            System.err.println("line:"+line+"出错,未定义的标识符:"+token);
            this.midFileCreateSuccess = false;
            return;
        }
        System.out.println("["+categoryCode+","+token+"]");
        try {
            lexScanner.getFw().write("["+categoryCode+","+token+"]"+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int lookupSymbolTableList() {
        if(token.equals("")) {
            return 0;
        }
        //返回类别号
        int a1 = symbolTable.getSymbolTableList().indexOf(description)+1;
        int a2 = symbolTable.getSymbolTableList().indexOf(token)+1;
        return Math.max(a1,a2);
    }
}
