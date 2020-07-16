package LL1;

import common.*;
import inter.*;

import java.util.*;

public class RecursiveDescent {

    private Stack<Object> stack;                    // 运算时临时产生的变量存放于此
    private Map<String,String> arrayPointRecord;    // 用于生成四元式数组索引的指针
    private Stack<For> forRecordStack;       // 用于处理for循环需要替换部分

    /**
     * 构造函数
     * @param verbose 是否显示当前四元式
     */
    public RecursiveDescent(boolean verbose){
        stack = new Stack<>();
        arrayPointRecord = new HashMap<>();
        forRecordStack = new Stack<>();
        program();
        if (verbose)
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
        System.out.println("语法分析成功");
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
     * 声明语句处理，当前array暂时只支持声明，不能赋值
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
                    // 改为 expr -- 2020-7-12 10：58
                    Token t = peek();
                    value = (int)wrapExpr();
                    SymbolTable.SYMBOLES.put(idName, new Num(Tag.INT, String.valueOf(value), t.getLine(), value, true));
                    genAssignCode(idName);
                }else if (peek().getType() == Tag.LEFT_SQUARE_BRACKET){ // array declaration
                    move();
                    Token t = peek();
                    value = Integer.parseInt(match(Tag.NUMINT).getContent());
                    match(Tag.RIGHT_SQUARE_BRACKET);
                    if (!(expect(Tag.SEMICOLON) || expect(Tag.COMMA))) // end error
                        error();
                    SymbolTable.SYMBOLES.put(idName,new Array(value,Tag.ARRAYINT,t.getLine()));
                    genArrayDeclarationCode(idName,value);
                }
                else {  // 不存在赋值操作，初始化 value 为 0
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
                    // 改为 expr -- 2020-7-12 10：58
                    Token t = peek();
                    value = wrapExpr();
                    SymbolTable.SYMBOLES.put(idName, new Real(Tag.DOUBLE, String.valueOf(value), t.getLine(), value, true));
                    genAssignCode(idName);
                }else if (peek().getType() == Tag.LEFT_SQUARE_BRACKET){ // array declaration
                    move();
                    Token t = peek();
                    int v = Integer.parseInt(match(Tag.NUMINT).getContent());
                    match(Tag.RIGHT_SQUARE_BRACKET);
                    if (!(expect(Tag.SEMICOLON) || expect(Tag.COMMA))) // end error
                        error();
                    SymbolTable.SYMBOLES.put(idName,new Array(v,Tag.ARRAYDOUBLE,t.getLine()));
                    // TODO: array gen
                    genArrayDeclarationCode(idName,v);
                }
                else {  // 不存在赋值操作，初始化 value 为 0
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
                if (forRecordStack.size() > 0 && forRecordStack.peek().getForStatAssign())
                    forRecordStack.peek().getForStatRecord().add(FourFormula.getLine());
                // add assign four formula
                genAssignCode(idName);
            }else if (expect(Tag.LEFT_SQUARE_BRACKET)){
                move();
                int index = getArrayIndex();
                arrayPointRecord.put(idName,stack.peek().toString());
                Array array;
                if (SymbolTable.SYMBOLES.get(idName) instanceof Array)
                    array = (Array)  SymbolTable.SYMBOLES.get(idName);
                else
                    throw new MyException(MyException.ARRAYERROR,t.getLine());
                if (index < 0 || index >= array.getSize())
                    throw new MyException(MyException.ARRAYINDEXOUTOFBOUNDS,peek().getLine());
                match(Tag.RIGHT_SQUARE_BRACKET);
                if (expect(Tag.ASSIGN)){    // 数组赋值
                    move();
                    if (expect(Tag.SEMICOLON) || expect(Tag.COMMA)) // end error
                        error();
                    double value = wrapExpr();
                    if (array.getType() == Tag.ARRAYINT){
                        array.getArray().set(index,(int)value);
                    }else{
                        array.getArray().set(index,value);
                    }
                    if (forRecordStack.size() > 0 && forRecordStack.peek().getForStatAssign())
                        forRecordStack.peek().getForStatRecord().add(FourFormula.getLine());
                    genAssignCode(idName+"["+arrayPointRecord.get(idName)+"]");
                }
            }
            if (forRecordStack.size() > 0 && !forRecordStack.peek().getForStatAssign())
                match(Tag.SEMICOLON);   // 暂时设置为赋值语句一定以;结尾
        }
    }

    /**
     * 用于数组赋值语句的索引操作
     * @return token代表的索引值
     */
    private int getArrayIndex(){
        int index;
        double value = wrapExpr();
        if (isIntegerForDouble(value))
            index = (int) value;
        else{
            System.err.println("数组索引必须是整型数字");
            throw new MyException(MyException.ARRAYINDEXERROR,peek().getLine());
        }
        return index;
    }

    /**
     * 判断double是否是整数
     * @param obj 待判断数字
     * @return 是否为整型数字
     */
    private boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj-Math.floor(obj) < eps;
    }

    /**
     * while语句
     * while本身可分解为if语句，不同的是在if语句最后面加一个强制跳转至if语句开始，并进行判断
     */
    private void whileStat(){
        match(Tag.WHILE);match(Tag.LEFT_BRACKET);
        ArrayList<String> list = bool();
        int ifBefore = If.gen(list.get(1),list.get(0),list.get(2));
        match(Tag.RIGHT_BRACKET);
        if (expect(Tag.LEFT_FBRACKET))
            block();
        else
            stat();
        While.genBackStat(ifBefore-1);  // 跳转至if条件判断语句处
        If.backPatch(ifBefore);
    }

    private void forStat(){
        match(Tag.FOR);match(Tag.LEFT_BRACKET);
        assign();   // for语句第一个赋值语句
        if (expect(Tag.SEMICOLON))
            move();
        ArrayList<String> list = bool();    // for语句第二个比较语句
        int ifBefore = If.gen(list.get(1),list.get(0),list.get(2));
        if (expect(Tag.SEMICOLON))
            move();

        forRecordStack.push(new For(true));
        assign();   // for语句第三个赋值语句
        forRecordStack.peek().setForStatAssign(false);
        match(Tag.RIGHT_BRACKET);

        /***************************************/
        int start = forRecordStack.peek().getForStatRecord().get(0);
        ArrayList<FourFormula> arrayList = new ArrayList<>();
        for (int i = 0; i < forRecordStack.peek().getForStatRecord().size(); i++) {
            arrayList.add(SymbolTable.fourFormulas.remove(start));
        }
        FourFormula.setLine(FourFormula.getLine()-forRecordStack.peek().getForStatRecord().size());
        /**************************************/

        if (expect(Tag.LEFT_FBRACKET))
            block();
        else
            stat();

        /**************************************/
        SymbolTable.fourFormulas.addAll(arrayList);
        FourFormula.setLine(FourFormula.getLine()+forRecordStack.peek().getForStatRecord().size());
        forRecordStack.pop();
        /**************************************/

        While.genBackStat(ifBefore-1);  // 跳转至if条件判断语句处
        If.backPatch(ifBefore);
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
                if (expect(Tag.IDENTIFY) || expect(Tag.IF) || expect(Tag.WHILE) || expect(Tag.FOR))
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
        else if (expect(Tag.WHILE))
            whileStat();
        else if (expect(Tag.FOR))
            forStat();
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
            // 表达式结束标志，逗号分号，比较符号，右括号（if语句使用），右方括号（数组使用）
            if (expect(Tag.SEMICOLON) || expect(Tag.COMMA) || isComparableSymbols(peek().getType())
                    || expect(Tag.RIGHT_BRACKET) || expect(Tag.RIGHT_SQUARE_BRACKET))   // 表达式读取完毕
                break;
            Token token = peek();
            if(token.getType() == Tag.PLUS || token.getType() == Tag.MINUS){
                move();
                if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                    throw new MyException(MyException.EXPRESSIONERROR, token.getLine());
                double value = term();
                if(token.getType() == Tag.PLUS){
                    if (forRecordStack.size() > 0 && forRecordStack.peek().getForStatAssign())
                        forRecordStack.peek().getForStatRecord().add(FourFormula.getLine());
                    genCode(SymbolTable.TAG2SYMBOL.get(Tag.PLUS));
                    result += value;
                }
                else{
                    if (forRecordStack.size() > 0 && forRecordStack.peek().getForStatAssign())
                        forRecordStack.peek().getForStatRecord().add(FourFormula.getLine());
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
            if (expect(Tag.SEMICOLON) || expect(Tag.COMMA) || isComparableSymbols(peek().getType())
                    || expect(Tag.RIGHT_BRACKET) || expect(Tag.RIGHT_SQUARE_BRACKET))   // 表达式读取完毕
                break;
            Token token = peek();
            if(token.getType() == Tag.MULTI || token.getType() == Tag.DIV){
                move();
                if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                    throw new MyException(MyException.EXPRESSIONERROR,token.getLine());
                double value = factor();
                if(token.getType() == Tag.MULTI){
                    if (forRecordStack.size() > 0 && forRecordStack.peek().getForStatAssign())
                        forRecordStack.peek().getForStatRecord().add(FourFormula.getLine());
                    genCode(SymbolTable.TAG2SYMBOL.get(Tag.MULTI));
                    result *= value;
                } else {
                    if(value == 0)
                        throw new MyException(MyException.DIVERROR,token.getLine());
                    if (forRecordStack.size() > 0 && forRecordStack.peek().getForStatAssign())
                        forRecordStack.peek().getForStatRecord().add(FourFormula.getLine());
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
            move();
            if (!expect(Tag.LEFT_SQUARE_BRACKET)){  // not array
                if (tmp.getType() != Tag.INT && tmp.getType() != Tag.DOUBLE)
                    error();
                stack.push(token.getContent());
                if (tmp.getType() == Tag.INT)
                    result = Integer.parseInt(tmp.getContent());
                else
                    result = Double.parseDouble(tmp.getContent());
            }else{  // an array
                move();     // skip '['
                if (!(expect(Tag.IDENTIFY) || expect(Tag.NUMDOUBLE) || expect(Tag.NUMINT) || expect(Tag.LEFT_BRACKET)))
                    throw new MyException(MyException.EXPRESSIONERROR,token.getLine());
                result = expr();
                if (!isIntegerForDouble(result)){
                    System.out.println("数组索引非整型数值");
                    throw new MyException(MyException.ARRAYINDEXERROR,peek().getLine());
                }
                int index = (int)result;
                if (!(SymbolTable.SYMBOLES.get(token.getContent()) instanceof Array))
                    throw new MyException(MyException.ARRAYIDENTIFYNOTFOUNDERROR,peek().getLine());
                Array array = (Array) SymbolTable.SYMBOLES.get(token.getContent());
                if (index < 0 || index >= array.getSize())
                    throw new MyException(MyException.ARRAYINDEXOUTOFBOUNDS,peek().getLine());
                result = array.getArray().get(index).intValue();
                match(Tag.RIGHT_SQUARE_BRACKET);    // match ']'
                stack.push(token.getContent()+"["+stack.pop().toString()+"]");  // 生成数组变量存入运算栈
            }
        }else if (expect(Tag.SEMICOLON) || expect(Tag.COMMA) || isComparableSymbols(peek().getType()))   // , ; comparableSymbols do nothing
            ;
        else
            error();
        return result;
    }

    /**
     * Object 类转字符串，用于四元式
     * @param obj 需要转换的变量
     * @return  转换后的
     */
    private String object2String(Object obj){
        String res;
        if (obj instanceof Integer)
            res = String.valueOf(obj);
        else if (obj instanceof Double)
            res = String.valueOf(obj);
        else if (obj instanceof String)
            res = obj.toString();
        else
            res = null;
        return res;
    }

    /**
     * 生成四元式
     * @param op 操作符
     */
    private void genCode(String op){
        Object arg2 = stack.pop();
        Object arg1 = stack.pop();
        String newTmpVariable = SymbolTable.getNewTmpVariable();
        FourFormula.gen(op,object2String(arg1),object2String(arg2),newTmpVariable);
        stack.push(newTmpVariable);
    }

    /**
     * 赋值语句生成四元式
     * @param name 需要与临时变量替换的id
     */
    private void genAssignCode(String name){
        Object arg1 = stack.pop();
        stack.push(name);
        FourFormula.gen("=",object2String(arg1),null,name);
    }

    /**
     * 对于未赋值的声明语句，默认将id赋值为0
     * @param name 未赋值id
     */
    private void genNotAssignCode(String name){
        FourFormula.gen("=","0",null,name);
    }

    /**
     * 对于数组声明，需要生成四元式
     * @param name 数组名称
     * @param size 数组大小
     */
    private void genArrayDeclarationCode(String name,int size){
        FourFormula.gen("array",object2String(size),null,name);
    }
}

/*
表达式求值递归写法
https://blog.csdn.net/qq_36081539/article/details/79678090?utm_source=blogxgwz0
expr = term | + | -
term = factor | * | /
factor = (expr) | num
 */