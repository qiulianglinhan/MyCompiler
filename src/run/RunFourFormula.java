package run;

import common.MyException;
import common.SymbolTable;
import common.Tag;
import inter.FourFormula;
import inter.InterArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: 未完成将三地址码写入文件操作
public class RunFourFormula {

    private int curPoint = 0;
    private Map<String,Double> map = new HashMap<>();
    private Map<String, InterArray>  arrayMap = new HashMap<>();

    public RunFourFormula(){
        new Result();
        while (curPoint < SymbolTable.fourFormulas.size()){
            parseFourFormula();
        }
    }

    private void parseFourFormula(){
        FourFormula fourFormula = SymbolTable.fourFormulas.get(curPoint++);
        String op = fourFormula.getOp();
        String arg1 = fourFormula.getArg1();
        String arg2 = fourFormula.getArg2();
        String res = fourFormula.getResult();
        if (op.equals("=")){   // assign
            double value = getArgValue(arg1);
            if (!res.contains("[")){
                map.put(res,value);
                if (res.charAt(0) != '$')   // 非临时变量放入结果集
                    Result.RESULT.put(res,value);
            }
            else{   // 数组元素赋值
                int leftSquareBracketIndex = res.indexOf('[');
                int rightSquareBracketIndex = res.indexOf(']');
                String index = res.substring(leftSquareBracketIndex+1,rightSquareBracketIndex);
                String idName = res.substring(0,leftSquareBracketIndex);
                int arrayIndex;
                if (Character.isDigit(index.charAt(0)))
                    arrayIndex = Integer.parseInt(index);
                else
                    arrayIndex = map.get(index).intValue();
                arrayMap.get(idName).getArray().set(arrayIndex,value);
                Result.ARRAYRESULT.get(idName).set(arrayIndex,value);
            }

        }else if (SymbolTable.COMPAREWORDS.contains(SymbolTable.SYMBOL2TAG.get(op))){
            if (compare(op,getArgValue(arg1),getArgValue(arg2)))   // 满足条件，走result的goto
                curPoint++; // if返回true时候，当前指针实际是 if的下面第二行
        }else if (op.equals("goto")){
            curPoint = Integer.parseInt(res);
        }else if (SymbolTable.operator.contains(op)){
            double value = getOperatorValue(op,getArgValue(arg1),getArgValue(arg2));
            map.put(res,value);
            if (res.charAt(0) != '$')
                Result.RESULT.put(res,value);
        }else if (op.equals("array")){
            arrayMap.put(res,new InterArray((int)getArgValue(arg1)));
            ArrayList<Number> arrayList = new ArrayList<>();
            int size = (int)getArgValue(arg1);
            initAnArray(size,arrayList);
            Result.ARRAYRESULT.put(res,arrayList);
        }
        else
            throw new MyException(MyException.FOURFORMULAERROR,curPoint);
    }

    /**
     * 从 map 中取出 arg 对应的 value
     * @param arg 需要获取 value 值得参量
     * @return map 中 arg 对应的 value
     */
    private double getArgValue(String arg){
        double value;
        if (Character.isDigit(arg.charAt(0)))
            value = Double.parseDouble(arg);
        else
            value = map.get(arg);
        return value;
    }

    /**
     * bool 跳转语句是否需要跳转
     * @param op 比较符号
     * @param value1 左边 value
     * @param value2 右边 value
     * @return true就走goto，false走下一条的goto语句
     */
    private boolean compare(String op, double value1, double value2){
        int tag = SymbolTable.SYMBOL2TAG.get(op);
        if (tag == Tag.EQ)
            return Double.compare(value1,value2) == 0;
        else if (tag == Tag.GT)
            return value1 > value2;
        else if (tag == Tag.GE)
            return value1 >= value2;
        else if (tag == Tag.LW)
            return value1 < value2;
        else if (tag == Tag.LE)
            return value1 <= value2;
        else    // Tag.NE 不等于
            return value1 != value2;
    }

    /**
     * 四元式计算
     * @param op 操作符
     * @param value1 第一个操作数
     * @param value2 第二个操作数
     * @return 计算后的值
     */
    private double getOperatorValue(String op, double value1, double value2){
        int tag = SymbolTable.SYMBOL2TAG.get(op);
        if (tag == Tag.PLUS)
            return value1+value2;
        else if (tag == Tag.MINUS)
            return value1-value2;
        else if (tag == Tag.MULTI)
            return value1*value2;
        else if (tag == Tag.DIV)
            return value1/value2;
        else
            throw new MyException(MyException.FOURFORMULAERROR,curPoint);
    }

    /**
     * 初始化数组
     * @param size 数组大小
     * @param arrayList 待初始化的数组
     */
    private void initAnArray(int size, ArrayList<Number> arrayList){
        for (int i = 0; i < size; i++) {
            arrayList.add(0);
        }
    }

}
