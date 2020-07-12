package LL1;

import common.*;
import inter.Else;
import inter.FourFormula;
import inter.If;

import java.util.ArrayList;
import java.util.Stack;

public class RecursiveDescent {

    private Stack<Object> stack;


    public RecursiveDescent(){
        stack = new Stack<>();
        program();
        for (int i = 0; i < SymbolTable.fourFormulas.size(); i++) {
            System.out.println(i+":"+SymbolTable.fourFormulas.get(i));
        }
    }

    /**
     * 当前分析栈期望符号
     * @param type Tag类型
     * @return 是否为期盼符号
     */
    private boolean expect(int type){
        if (SymbolTable.TOEKNS.peek() == null)
            return false;
        else
            return SymbolTable.TOEKNS.peek().getType() == type;
    }

    /**
     * 移动
     * @return 返回移动的Token（如果有需要）
     */
    private Token move(){
        return SymbolTable.TOEKNS.pop();
    }

    /**
     * 匹配并向前移动
     * @param type Tag 类型
     * @return 是期盼Token，返回token；否则出错
     */
    private Token match(int type){
        if (expect(type))
            return move();
        matchError(type);
        return null;
    }

    private Token peek(){
        return SymbolTable.TOEKNS.peek();
    }

    /**
     * 语法分析出错
     */
    private void error(){
        throw new MyException(MyException.SYNTAXERROR,SymbolTable.TOEKNS.peek().getLine());
    }

    /**
     * 匹配错误
     * @param type 期望匹配的 type 错误
     */
    private void matchError(int type){
        System.err.println("未找到"+SymbolTable.TAG2SYMBOL.get(type));
        error();
    }

    /**
     * 程序开始
     */
    private void program(){
        // program begin
        match(Tag.INT);match(Tag.MAIN);match(Tag.LEFT_BRACKET);
        match(Tag.RIGHT_BRACKET);match(Tag.LEFT_FBRACKET);

        // c 语言开始是声明语句
        declaration(-1);

        // 执行程序体
        body();

        // program end
        match(Tag.RETURN);match(Tag.NUMINT);match(Tag.SEMICOLON);
        match(Tag.RIGHT_FBRACKET);
    }

    /**
     * 程序体
     */
    private void body(){
        if (expect(Tag.IF))
            ifStat();
        else if (expect(Tag.ELSE)) {  // 悬空 else
            System.err.println(peek().getLine()+"行出现悬空else");
            error();
        }
        else
            stat();
        // TODO: 简单起见，主函数只有一个 return 语句
        if (!expect(Tag.RETURN))
            body();
    }

    /**
     * 声明语句处理
     * @param type 声明变量类型，-1 为 "," 分割时传递变量使用
     */
    private void declaration(int type){
        if (peek() != null){
            int tmpType = type == -1 ? peek().getType() : type;   // 声明语句开始必须是变量类型
            if (tmpType == Tag.INT) {
                if (type == -1) // 非继承属性，不需要移动
                    move();
                String idName = match(Tag.IDENTIFY).getContent();
                int value = 0;
                if (peek().getType() == Tag.ASSIGN) {    // 存在赋值操作
                    move();
                    if (expect(Tag.SEMICOLON) || expect(Tag.COMMA)) // end error
                        error();
                    /*
                    Token t = move();
                    if (t.getType() == Tag.NUMINT)
                        value = Integer.parseInt(t.getContent());
                    SymbolTable.SYMBOLES.put(idName, new Num(Tag.INT, t.getContent(), t.getLine(), value, true));
                     */
                    // 改为 expr -- 2020-7-12 10：58
                    Token t = peek();
                    value = (int)wrapExpr();
                    SymbolTable.SYMBOLES.put(idName, new Num(Tag.INT, String.valueOf(value), t.getLine(), value, true));
                    genAssignCode(idName);
                } else {  // 不存在赋值操作，初始化 value 为 0
                    SymbolTable.SYMBOLES.put(idName, new Num(Tag.INT, "0", peek().getLine(), 0, false));
                    genNotAssignCode(idName);
                }
                if (expect(Tag.COMMA)) {
                    move(); // skip ,
                    declaration(Tag.INT);   // 继承变量类型
                } else if (expect(Tag.SEMICOLON)) {
                    move(); // skip ;
                    declaration(-1);
                } else
                    error();
                return;
            }else if (tmpType == Tag.DOUBLE) {
                if (type == -1)
                    move();
                String idName = match(Tag.IDENTIFY).getContent();
                double value = 0.0;
                if (peek().getType() == Tag.ASSIGN) {    // 存在赋值操作
                    move();
                    if (expect(Tag.SEMICOLON) || expect(Tag.COMMA)) // end error
                        error();
                    /*
                    Token t = move();
                    if (t.getType() == Tag.NUMDOUBLE)
                        value = Double.parseDouble(t.getContent());
                    SymbolTable.SYMBOLES.put(idName, new Real(Tag.DOUBLE, t.getContent(), t.getLine(), value, true));

                     */
                    // 改为 expr -- 2020-7-12 10：58
                    Token t = peek();
                    value = wrapExpr();
                    SymbolTable.SYMBOLES.put(idName, new Real(Tag.DOUBLE, String.valueOf(value), t.getLine(), value, true));
                    genAssignCode(idName);

                } else {  // 不存在赋值操作，初始化 value 为 0
                    SymbolTable.SYMBOLES.put(idName, new Real(Tag.DOUBLE, "0.0", peek().getLine(), 0, false));
                    genNotAssignCode(idName);
                }
                if (expect(Tag.COMMA)) {
                    move(); // skip ,
                    declaration(Tag.DOUBLE);   // 继承变量类型
                } else if (expect(Tag.SEMICOLON)) {
                    move(); // skip ;
                    declaration(-1);
                } else
                    error();
                return;
            }
        }
    }

    /**
     * 赋值语句
     * 暂时设置为赋值语句一定以 ";" 结尾
     */
    private void assign(){
        if (expect(Tag.IDENTIFY)){
            Token t = move();
            assert t != null;   // t must not null
            assert t.getContent() != null && !t.getContent().equals("");  // content must not null
            String idName = t.getContent();
            if (SymbolTable.SYMBOLES.get(idName) == null)
                throw new MyException(MyException.IDENTIFYNOTFOUND,t.getLine());
            t = SymbolTable.SYMBOLES.get(idName);   // 原始 token 存储数字
            if (expect(Tag.ASSIGN)){
                move();
                if (expect(Tag.SEMICOLON) || expect(Tag.COMMA)) // end error
                    error();
                double value = wrapExpr();
                if (t.getType() == Tag.INT){
                    Num num = (Num)t;
                    SymbolTable.SYMBOLES.put(idName,new Num(Tag.INT,String.valueOf((int)value),t.getLine(),(int)value,num.isInit()));
                }else{
                    Real real = (Real)t;
                    SymbolTable.SYMBOLES.put(idName,new Real(Tag.DOUBLE,String.valueOf(value),t.getLine(),value,real.isInit()));
                }
                // add assign four formula
                genAssignCode(idName);
            }
            match(Tag.SEMICOLON);   // 暂时设置为赋值语句一定以;结尾
        }
//        if (expect(Tag.IDENTIFY))
//            assign();
//        if (expect(Tag.IF))
//            ifStat();
    }


    /**
     * if 条件句
     */
    private void ifStat(){
        match(Tag.IF);match(Tag.LEFT_BRACKET);
        ArrayList<String> list = bool();
        int ifBefore = If.gen(list.get(1),list.get(0),list.get(2));
        match(Tag.RIGHT_BRACKET);
        if (expect(Tag.LEFT_FBRACKET))
            block();
        else
            stat();
        // 回填
        If.backPatch(ifBefore);
        if (expect(Tag.ELSE)){
            move();
            int elseBefore = Else.gen();
            if (expect(Tag.LEFT_FBRACKET))
                block();
            else
                stat();
            Else.backPatch(ifBefore,elseBefore);
        }
    }

    /**
     * block 块，以 "{" 打头，以 "}"结束，中间是 statement
     */
    private void block(){
        if (expect(Tag.LEFT_FBRACKET)){ // { 打头
            match(Tag.LEFT_FBRACKET);
            while (true){   // 多条 statements
                if (expect(Tag.IDENTIFY) || expect(Tag.IF))
                    stat();
                else
                    break;
            }
            match(Tag.RIGHT_FBRACKET);  // } 结束 block
        }
    }


    /**
     * statement 语句，以 ";" 结束
     */
    private void stat(){
        if (expect(Tag.IDENTIFY))
            assign();
        else if (expect(Tag.IF))
            ifStat();
    }

    /**
     * bool 条件句，用于判断
     * @return 返回结果列表，list[0]表示比较式左边表达式结果，list[1]表示比较符号，list[2]表示比较式右边表达式结果
     */
    private ArrayList<String> bool(){
        ArrayList<String> list = new ArrayList<>();
        wrapExpr();
        list.add(stack.peek().toString());
        if (!isComparableSymbols(peek().getType()))
            error();
        list.add(SymbolTable.TAG2SYMBOL.get(move().getType()));
        wrapExpr();
        list.add(stack.peek().toString());
        return list;
    }

    /**
     * 判断当前类型是否是比较字符
     * @param type 当前类型号
     * @return 是否是比较字符
     */
    private boolean isComparableSymbols(int type){
        return SymbolTable.COMPAREWORDS.contains(type);
    }

    /**
     * 包装 expr 表达式，此处为设计四元式而编写，注意stack只在调用该方法时才清空
     * 每次使用完不清空是用于其他方法需要当前栈中存放的变量名
     * @return 表达式值
     */
    private double wrapExpr(){
        stack.clear();
        return expr();
    }


    /**
     * 表达式求值
     * @return 表达式值
     */
    private double expr(){
        double result = term();
        boolean hasMore = true;
        while (hasMore){
            if (peek() == null)
                break;
            if (expect(Tag.SEMICOLON) || expect(Tag.COMMA) || isComparableSymbols(peek().getType()) || expect(Tag.RIGHT_BRACKET))   // 表达式读取完毕
                break;
            Token token = peek();
            if(token.getType() == Tag.PLUS || token.getType() == Tag.MINUS){
                move();
                if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                    throw new MyException(MyException.EXPRESSIONERROR, token.getLine());
                double value = term();
                if(token.getType() == Tag.PLUS){
                    genCode(SymbolTable.TAG2SYMBOL.get(Tag.PLUS));
                    result += value;
                }
                else{
                    genCode(SymbolTable.TAG2SYMBOL.get(Tag.MINUS));
                    result -= value;
                }

            }else{
                hasMore = false;
            }
        }
        return result;
    }

    /**
     * 项求值
     * @return 项的值
     */
    private double term(){
        double result = factor();
        while (true){
            if (peek() == null)
                break;
            if (expect(Tag.SEMICOLON) || expect(Tag.COMMA) || isComparableSymbols(peek().getType()) || expect(Tag.RIGHT_BRACKET))   // 表达式读取完毕
                break;
            Token token = peek();
            if(token.getType() == Tag.MULTI || token.getType() == Tag.DIV){
                move();
                if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                    throw new MyException(MyException.EXPRESSIONERROR,token.getLine());
                double value = factor();
                if(token.getType() == Tag.MULTI){
                    genCode(SymbolTable.TAG2SYMBOL.get(Tag.MULTI));
                    result *= value;
                }

                else{
                    if(value == 0)
                        throw new MyException(MyException.DIVERROR,token.getLine());
                    genCode(SymbolTable.TAG2SYMBOL.get(Tag.DIV));
                    result /= value;
                }
            }else
                break;
        }
        return result;
    }

    /**
     * 因子求值
     * @return 因子的值
     */
    private double factor(){
        double result = 0;
        Token token = peek();
        if(token.getType() == Tag.LEFT_BRACKET){    // 左括号，引入新的表达式
            move();    // 继续向后读
            if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                throw new MyException(MyException.EXPRESSIONERROR,token.getLine());
            result = expr();
            match(Tag.RIGHT_BRACKET);   // 读取右括号
        }else if (token.getType() == Tag.NUMINT){  // 常数
            result = Integer.parseInt(token.getContent());
            stack.push(token.getContent());
            move();
        }else if(token.getType() == Tag.NUMDOUBLE){
            result = Double.parseDouble(token.getContent());
            stack.push(token.getContent());
            move();
        }else if (token.getType() == Tag.IDENTIFY){ // id 转数字
            Token tmp = SymbolTable.SYMBOLES.get(token.getContent());
            if (tmp == null){
                System.err.println("未找到标识符"+token.getContent());
                error();
            }
            if (tmp.getType() != Tag.INT && tmp.getType() != Tag.DOUBLE)
                error();
            stack.push(token.getContent());
            if (tmp.getType() == Tag.INT)
                result = Integer.parseInt(tmp.getContent());
            else
                result = Double.parseDouble(tmp.getContent());
            move();
        }else if (expect(Tag.SEMICOLON) || expect(Tag.COMMA) || isComparableSymbols(peek().getType()))   // , ; do nothing
            ;
        else
            error();
        return result;
    }

    /**
     * 生成四元式
     * @param op 操作符
     */
    private void genCode(String op){
        Object arg2 = stack.pop();
        Object arg1 = stack.pop();
        String newTmpVariable = SymbolTable.getNewTmpVariable();
        FourFormula.gen(op,arg1,arg2,newTmpVariable);
        stack.push(newTmpVariable);
    }

    /**
     * 赋值语句生成四元式
     * @param name 需要与临时变量替换的id
     */
    private void genAssignCode(String name){
        Object arg1 = stack.pop();
        stack.push(name);
        FourFormula.gen("=",arg1,null,name);
    }

    /**
     * 对于未赋值的声明语句，默认将id赋值为0
     * @param name 未赋值id
     */
    private void genNotAssignCode(String name){
        FourFormula.gen("=",0,null,name);
    }
}

/*
表达式求值递归写法
https://blog.csdn.net/qq_36081539/article/details/79678090?utm_source=blogxgwz0
expr = term | + | -
term = factor | * | /
factor = (expr) | num
 */