package lexer.departed;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class SymbolTable {
    private List<String> symbolTableList = new ArrayList<>();

    SymbolTable() {
        createSymbolTable();
    }

    public void createSymbolTable() {

        //构造符号表
        symbolTableList.add("标识符");
        symbolTableList.add("无符号整数");
        symbolTableList.add("无符号小数");
        symbolTableList.add("单字符分界符");

        symbolTableList.add("void");
        symbolTableList.add("int");
        symbolTableList.add("float");
        symbolTableList.add("double");
        symbolTableList.add("if");
        symbolTableList.add("else");
        symbolTableList.add("for");
        symbolTableList.add("do");
        symbolTableList.add("while");
        symbolTableList.add("main");
        symbolTableList.add("return");

        symbolTableList.add("加法");
        symbolTableList.add("减法");
        symbolTableList.add("位与");
        symbolTableList.add("位或");
        symbolTableList.add("大于");
        symbolTableList.add("小于");
        symbolTableList.add("乘法");
        symbolTableList.add("除法");
        symbolTableList.add("取反");
        symbolTableList.add("赋值");
        symbolTableList.add("大于等于");
        symbolTableList.add("右移");
        symbolTableList.add("小于等于");
        symbolTableList.add("左移");
        symbolTableList.add("不等于");
        symbolTableList.add("等于");
        symbolTableList.add("左注释");
        symbolTableList.add("右注释");
        symbolTableList.add("单行注释");
        symbolTableList.add("加等于");
        symbolTableList.add("减等于");
        symbolTableList.add("乘等于");
        symbolTableList.add("除等于");
        symbolTableList.add("与等于");
        symbolTableList.add("或等于");
        symbolTableList.add("加加");
        symbolTableList.add("减减");
        symbolTableList.add("与");
        symbolTableList.add("或");
    }

    public synchronized List<String> getSymbolTableList() {
        return symbolTableList;
    }
}
