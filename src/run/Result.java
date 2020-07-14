package run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 存放四元式运行结果
 */
public class Result {
    public static Map<String,Double> RESULT;  // 存放非数组值结果
    public static Map<String, ArrayList<Number>> ARRAYRESULT;   // 存放数组结果
    static {
        RESULT = new HashMap<>();
        ARRAYRESULT = new HashMap<>();
    }
}
