package LL1;

import common.*;

public class RecursiveDescent {


    public RecursiveDescent(){
        program();
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
        else
            stat();
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
                    Token t = move();
                    if (t.getType() == Tag.NUMINT)
                        value = Integer.parseInt(t.getContent());
                    SymbolTable.SYMBOLES.put(idName, new Num(Tag.INT, t.getContent(), t.getLine(), value, true));
                } else  // 不存在赋值操作，初始化 value 为 0
                    SymbolTable.SYMBOLES.put(idName, new Num(Tag.INT, "0", peek().getLine(), 0, false));
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
                    Token t = move();
                    if (t.getType() == Tag.NUMDOUBLE)
                        value = Double.parseDouble(t.getContent());
                    SymbolTable.SYMBOLES.put(idName, new Real(Tag.DOUBLE, t.getContent(), t.getLine(), value, true));
                } else  // 不存在赋值操作，初始化 value 为 0
                    SymbolTable.SYMBOLES.put(idName, new Real(Tag.DOUBLE, "0.0", peek().getLine(), 0, false));
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
                double value = expr();
                if (t.getType() == Tag.INT){
                    Num num = (Num)t;
                    SymbolTable.SYMBOLES.put(idName,new Num(Tag.INT,String.valueOf((int)value),t.getLine(),(int)value,num.isInit()));
                }else{
                    Real real = (Real)t;
                    SymbolTable.SYMBOLES.put(idName,new Real(Tag.DOUBLE,String.valueOf(value),t.getLine(),value,real.isInit()));
                }
            }
            match(Tag.SEMICOLON);   // 暂时设置为赋值语句一定以;结尾
        }
    }


    /**
     * if 条件句
     */
    private void ifStat(){

    }

    /**
     * block 块，以 "{" 打头，以 "}"结束，中间是 statement
     */
    private void block(){
        if (expect(Tag.LEFT_FBRACKET)){ // { 打头
            match(Tag.LEFT_FBRACKET);

            match(Tag.RIGHT_FBRACKET);  // } 结束 block
        }

    }


    /**
     * statement 语句，以 ";" 结束
     */
    private void stat(){
        if (expect(Tag.IDENTIFY))
            assign();
    }

    /**
     * bool 条件句，用于判断
     */
    private void bool(){

    }

    /**
     * 表达式求值
     * @return 表达式值
     */
    private double expr(){
        double result = term();
        boolean hasMore = true;
        while (hasMore){
            if (expect(Tag.SEMICOLON) || expect(Tag.COMMA))   // 表达式读取完毕
                break;
            Token token = peek();
            if(token.getType() == Tag.PLUS || token.getType() == Tag.MINUS){
                move();
                if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                    throw new MyException(MyException.EXPRESSIONERROR, token.getLine());
                double value = term();
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

    /**
     * 项求值
     * @return 项的值
     */
    private double term(){
        double result = factor();
        while (true){
            if (expect(Tag.SEMICOLON) || expect(Tag.COMMA))   // 表达式读取完毕
                break;
            Token token = peek();
            if(token.getType() == Tag.MULTI || token.getType() == Tag.DIV){
                move();
                if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                    throw new MyException(MyException.EXPRESSIONERROR,token.getLine());
                double value = factor();
                if(token.getType() == Tag.MULTI)
                    result *= value;
                else{
                    if(value == 0)
                        throw new MyException(MyException.DIVERROR,token.getLine());
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
            move();
        }else if(token.getType() == Tag.NUMDOUBLE){
            result = Double.parseDouble(token.getContent());
            move();
        }else if (token.getType() == Tag.IDENTIFY){ // id 转数字
            Token tmp = SymbolTable.SYMBOLES.get(token.getContent());
            if (tmp == null)
                error();
            if (tmp.getType() != Tag.INT && tmp.getType() != Tag.DOUBLE)
                error();
            if (tmp.getType() == Tag.INT)
                result = Integer.parseInt(tmp.getContent());
            else
                result = Double.parseDouble(tmp.getContent());
            move();
        }else if (expect(Tag.SEMICOLON) || expect(Tag.COMMA))   // , ; do nothing
            ;
        else{
            throw new MyException(-1,-1);
        }
        return result;
    }

}

