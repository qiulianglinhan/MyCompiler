package run;

import common.MyException;
import common.SymbolTable;
import common.Tag;
import inter.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class RunFourFormula {

    private int curPoint;                                           // 当前四元式执行位置
    private Map<String,Double> idMap = new HashMap<>();             // id -> value
    private Map<String, InterArray>  arrayMap = new HashMap<>();    // id -> array
    private Map<String,Double> tmpIdMap = new HashMap<>();          // 参数临时map
    private double returnValue;                                     // return value
    private int parameterCount;                                     // 统计执行当前call function时已传入参数个数
    private Stack<String> functionNameStack;                        // 存放运行函数名称
    private String currentRunFunctionName;                          // 当前运行函数名称

    /**
     * 运行四元式，当前四元式执行是从内存直接运行，生成四元式文件仅仅为用户查看
     * @param generateFourFormulaFile 是否生成四元式文件
     * @param fileName 源文件名称，以.c结尾的文件，自动生成与.c文件名称相同的.ff文件，.ff文件为生成四元式文件
     * @throws IOException 生成四元式文件错误
     */
    public RunFourFormula(boolean generateFourFormulaFile,String fileName) throws IOException {
        if (generateFourFormulaFile){
            String outMidFileName = fileName.substring(0,fileName.indexOf('.'))+".ff";
            File fout = new File(outMidFileName);
            FileWriter fw = new FileWriter(fout);
            for (FourFormula fourFormula : SymbolTable.fourFormulas) {
                fw.write(fourFormula.toString()+"\n");
            }
            fw.close();
            System.out.println("生成四元式文件成功，生成文件名称为："+outMidFileName);
        }
        if (Function.allFunctions.get("main") == null)
            throw new MyException(MyException.MAINFUNCTIONNOTFOUNDERROR,MyException.NEEDNOTLINENUMBER);
        curPoint = Function.allFunctions.get("main").getStartLineNum();
        currentRunFunctionName = "main";
        functionNameStack = new Stack<>();
        parameterCount = 0;
        while (curPoint < SymbolTable.fourFormulas.size()){
            parseFourFormula();
        }
    }

    private void parseFourFormula(){

        // handler void function
        if (!currentRunFunctionName.equals("main") && curPoint > Function.allFunctions.get(currentRunFunctionName).getEndLineNum()){
            idMap.clear();
            arrayMap.clear();
            Result.RESULT.clear();
            Result.ARRAYRESULT.clear();
            idMap = Env.envStack.peek().getIdMap();
            arrayMap = Env.envStack.peek().getArrayMap();
            Result.RESULT = Env.envStack.peek().getResultIdMap();
            Map<String, InterArray> resultArrayMap = Env.envStack.peek().getResultArrayMap();
            for (String key : resultArrayMap.keySet()) {
                Result.ARRAYRESULT.put(key, resultArrayMap.get(key).getArray());
            }
            curPoint = Env.envStack.peek().getReturnPosition();
            Env.envStack.pop();
            currentRunFunctionName = functionNameStack.pop();
        }

        FourFormula fourFormula = SymbolTable.fourFormulas.get(curPoint++);

        String op = fourFormula.getOp();
        String arg1 = fourFormula.getArg1();
        String arg2 = fourFormula.getArg2();
        String res = fourFormula.getResult();
        if (op.equals("=")){   // assign
            double value = getArgValue(arg1);
            if (!res.contains("[")){
                idMap.put(res,value);
                if (res.charAt(0) != '$')   // 非临时变量放入结果集
                    Result.RESULT.put(res,value);
            } else {   // 数组元素赋值
                int leftSquareBracketIndex = res.indexOf('[');
                int rightSquareBracketIndex = res.indexOf(']');
                String index = res.substring(leftSquareBracketIndex+1,rightSquareBracketIndex);
                String idName = res.substring(0,leftSquareBracketIndex);
                int arrayIndex;
                if (Character.isDigit(index.charAt(0)))
                    arrayIndex = Integer.parseInt(index);
                else
                    arrayIndex = idMap.get(index).intValue();
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
            idMap.put(res,value);
            if (res.charAt(0) != '$')
                Result.RESULT.put(res,value);
        }else if (op.equals("array")){
            arrayMap.put(res,new InterArray((int)getArgValue(arg1)));
            ArrayList<Number> arrayList = new ArrayList<>();
            int size = (int)getArgValue(arg1);
            initAnArray(size,arrayList);
            Result.ARRAYRESULT.put(res,arrayList);
        }else if (op.equals("param")){
            String arg = fourFormula.getArg1();
            double value = getArgValue(arg);
            tmpIdMap.put(fourFormula.getResult(),value);
            parameterCount++;
        }else if (op.equals("call")){
            String functionName = fourFormula.getArg1();
            if (Function.allFunctions.get(functionName) == null)
                throw new MyException(MyException.FUNCTIONNOTFOUNDERROR,MyException.NEEDNOTLINENUMBER);
            int needParameterNumber = Integer.parseInt(fourFormula.getArg2());
            if (needParameterNumber != parameterCount){
                if (needParameterNumber < parameterCount)
                    System.err.println("参数传入数目多于需要数目");
                else
                    System.err.println("参数传入数目小于需要数目");
                throw new MyException(MyException.PARAMETERERROR,MyException.NEEDNOTLINENUMBER);
            }
            parameterCount = 0;     // reset parameter count

            Env.envStack.push(new AllIdMapAndArrayMap(idMap,arrayMap,curPoint));
            idMap.clear();
            tmpIdMap.forEach((k,v) -> idMap.put(k,v));
            curPoint = Function.allFunctions.get(functionName).getStartLineNum();

            Result.RESULT.forEach((k,v) -> Env.envStack.peek().getResultIdMap().put(k,v));
            Result.ARRAYRESULT.forEach((k,v) -> Env.envStack.peek().getResultArrayMap().put(k,new InterArray(v.size(),v)));

            tmpIdMap.clear();
            arrayMap.clear();
            Result.RESULT.clear();
            Result.ARRAYRESULT.clear();

            for (String key : idMap.keySet()) {
                if (key.charAt(0) != '$')
                    Result.RESULT.put(key,idMap.get(key));
            }

            tmpIdMap.clear();

            functionNameStack.push(currentRunFunctionName);
            currentRunFunctionName = functionName;
        }else if (op.equals("return")){
            if (Env.envStack.size() > 0) {
                returnValue = getArgValue(fourFormula.getResult());
                idMap.clear();
                arrayMap.clear();
                Result.RESULT.clear();
                Result.ARRAYRESULT.clear();
                idMap = Env.envStack.peek().getIdMap();
                arrayMap = Env.envStack.peek().getArrayMap();
                Result.RESULT = Env.envStack.peek().getResultIdMap();
                Map<String, InterArray> resultArrayMap = Env.envStack.peek().getResultArrayMap();
                for (String key : resultArrayMap.keySet()) {
                    Result.ARRAYRESULT.put(key, resultArrayMap.get(key).getArray());
                }
                int returnPosition = Env.envStack.peek().getReturnPosition();
                String result = SymbolTable.fourFormulas.get(returnPosition - 1).getResult();
                if (!result.equals("_")) {
                    if (result.contains("[")) { // 数组元素赋值
                        int leftSquareBracketIndex = res.indexOf('[');
                        int rightSquareBracketIndex = res.indexOf(']');
                        String index = res.substring(leftSquareBracketIndex + 1, rightSquareBracketIndex);
                        String idName = res.substring(0, leftSquareBracketIndex);
                        int arrayIndex;
                        if (Character.isDigit(index.charAt(0)))
                            arrayIndex = Integer.parseInt(index);
                        else
                            arrayIndex = idMap.get(index).intValue();
                        arrayMap.get(idName).getArray().set(arrayIndex, returnValue);
                        Result.ARRAYRESULT.get(idName).set(arrayIndex, returnValue);
                    } else {
                        idMap.put(result, returnValue);
                        if (result.charAt(0) != '$')
                            Result.RESULT.put(result, returnValue);
                    }
                }
                curPoint = Env.envStack.peek().getReturnPosition();
                Env.envStack.pop();
                currentRunFunctionName = functionNameStack.pop();
            } else
                curPoint = SymbolTable.fourFormulas.size();
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
        else if (arg.contains("[")){
            int leftSquareBracketIndex = arg.indexOf('[');
            int rightSquareBracketIndex = arg.indexOf(']');
            String index = arg.substring(leftSquareBracketIndex+1,rightSquareBracketIndex);
            String idName = arg.substring(0,leftSquareBracketIndex);
            int arrayIndex;
            if (Character.isDigit(index.charAt(0)))
                arrayIndex = Integer.parseInt(index);
            else
                arrayIndex = idMap.get(index).intValue();
            value = arrayMap.get(idName).getArray().get(arrayIndex).doubleValue();
        }else
            value = idMap.get(arg);
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
